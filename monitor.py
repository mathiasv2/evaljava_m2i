import argparse
import hashlib
import json
import os
import subprocess
import sys
import time
import urllib.error
import urllib.request
from datetime import datetime, timezone
from pathlib import Path
from typing import Dict, List, Optional

# ── Helpers ───────────────────────────────────────────────────────────────────

def get_tracked_files() -> List[str]:
    """Retourne les fichiers qui seraient inclus dans un commit git."""
    try:
        result = subprocess.run(
            ["git", "ls-files", "--cached", "--others", "--exclude-standard"],
            capture_output=True, text=True, check=True
        )
        return [f for f in result.stdout.splitlines() if f]
    except subprocess.CalledProcessError:
        print("[ERREUR] Ce dossier n'est pas un dépôt git.", file=sys.stderr)
        sys.exit(1)


def read_file_safe(path: str) -> Optional[str]:
    """Lit un fichier texte ; retourne None si binaire ou illisible."""
    try:
        return Path(path).read_text(encoding="utf-8", errors="replace")
    except (OSError, PermissionError):
        return None


def file_hash(content: str) -> str:
    return hashlib.sha256(content.encode()).hexdigest()


def count_lines(content: str) -> int:
    return content.count("\n") + (1 if content and not content.endswith("\n") else 0)


def diff_content(old: str, new: str) -> Dict:
    """Calcule les lignes ajoutées / supprimées (diff simplifié)."""
    old_lines = set(old.splitlines())
    new_lines = set(new.splitlines())
    added   = len(new_lines - old_lines)
    removed = len(old_lines - new_lines)
    return {"added": added, "removed": removed, "total_lines": count_lines(new)}


def now_iso() -> str:
    return datetime.now(timezone.utc).isoformat()


def send(server: str, payload: Dict, timeout: int = 10) -> bool:
    """Envoie un payload JSON via urllib (pas de dépendance externe)."""
    try:
        data = json.dumps(payload).encode("utf-8")
        req  = urllib.request.Request(
            "{}/event".format(server),
            data=data,
            headers={"Content-Type": "application/json"},
            method="POST",
        )
        with urllib.request.urlopen(req, timeout=timeout) as resp:
            return resp.status == 200
    except (urllib.error.URLError, OSError) as e:
        print("[WARN] Envoi échoué : {}".format(e), file=sys.stderr)
        return False


# ── Snapshot initial ──────────────────────────────────────────────────────────

def snapshot(server: str, student: str) -> Dict[str, str]:
    """Envoie tous les fichiers trackés ; retourne {path: hash}."""
    files = get_tracked_files()
    state: Dict[str, str] = {}
    payload_files: Dict   = {}

    for f in files:
        content = read_file_safe(f)
        if content is None:
            continue
        state[f] = file_hash(content)
        payload_files[f] = {
            "content": content,
            "lines":   count_lines(content),
        }

    payload = {
        "type":      "snapshot",
        "student":   student,
        "timestamp": now_iso(),
        "cwd":       os.getcwd(),
        "files":     payload_files,
    }

    ok     = send(server, payload)
    status = "✓" if ok else "✗ (serveur injoignable)"
    print("[{}] Snapshot initial envoyé ({} fichiers) {}".format(
        now_iso(), len(payload_files), status))
    return state


# ── Boucle de surveillance ────────────────────────────────────────────────────

def watch(server: str, student: str, interval: int, state: Dict[str, str]):
    print("[INFO] Surveillance démarrée — intervalle {}s  (Ctrl+C pour arrêter)\n".format(interval))
    while True:
        time.sleep(interval)
        files   = get_tracked_files()
        changed: Dict = {}

        for f in files:
            content = read_file_safe(f)
            if content is None:
                continue
            h        = file_hash(content)
            old_hash = state.get(f)

            if old_hash is None:
                # Nouveau fichier apparu
                changed[f] = {
                    "status":  "new",
                    "content": content,
                    "diff":    {"added": count_lines(content), "removed": 0,
                                "total_lines": count_lines(content)},
                }
            elif h != old_hash:
                # Fichier modifié — récupère l'ancien contenu via git show
                try:
                    res = subprocess.run(
                        ["git", "show", "HEAD:{}".format(f)],
                        capture_output=True, text=True
                    )
                    old_content = res.stdout if res.returncode == 0 else ""
                except Exception:
                    old_content = ""

                changed[f] = {
                    "status":  "modified",
                    "content": content,
                    "diff":    diff_content(old_content, content),
                }

            if f in changed:
                state[f] = h

        # Fichiers supprimés
        tracked_set = set(files)
        for f in list(state.keys()):
            if f not in tracked_set:
                changed[f] = {"status": "deleted", "content": "", "diff": {}}
                del state[f]

        if not changed:
            continue

        payload = {
            "type":             "diff",
            "student":          student,
            "timestamp":        now_iso(),
            "interval_seconds": interval,
            "files":            changed,
        }

        ok            = send(server, payload)
        total_added   = sum(v["diff"].get("added",   0) for v in changed.values())
        total_removed = sum(v["diff"].get("removed", 0) for v in changed.values())
        status        = "✓" if ok else "✗"
        print("[{}] Diff envoyé : {} fichier(s)  +{} / -{} lignes  {}".format(
            now_iso(), len(changed), total_added, total_removed, status))


# ── Entrypoint ────────────────────────────────────────────────────────────────

def main():
    parser = argparse.ArgumentParser(description="Agent de surveillance d'examen")
    parser.add_argument("--server",   required=True,
                        help="URL du serveur, ex: http://192.168.1.10:5000")
    parser.add_argument("--student",  required=True,
                        help="Nom de l'étudiant")
    parser.add_argument("--interval", type=int, default=10,
                        help="Intervalle entre diffs en secondes (défaut: 10)")
    args = parser.parse_args()

    print("=== Moniteur d'examen ===")
    print("Étudiant : {}".format(args.student))
    print("Serveur  : {}".format(args.server))
    print("Dossier  : {}\n".format(os.getcwd()))

    state = snapshot(args.server, args.student)
    try:
        watch(args.server, args.student, args.interval, state)
    except KeyboardInterrupt:
        print("\n[INFO] Surveillance arrêtée.")


if __name__ == "__main__":
    main()