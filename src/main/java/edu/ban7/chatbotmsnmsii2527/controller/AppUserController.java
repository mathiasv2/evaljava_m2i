package edu.ban7.chatbotmsnmsii2527.controller;

import edu.ban7.chatbotmsnmsii2527.dao.AppUserDao;
import edu.ban7.chatbotmsnmsii2527.model.AppUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AppUserController {

    protected final AppUserDao appUserDao;

    @GetMapping("/user/list")
    public List<AppUser> getAll() {
        return appUserDao.findAll();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<AppUser> get(@PathVariable int id) {
        Optional<AppUser> optionalAppUser = appUserDao.findById(id);

        if(optionalAppUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalAppUser.get(), HttpStatus.OK);

    }

    @PostMapping("/user")
    public ResponseEntity<AppUser> create(@RequestBody @Valid AppUser appUser) {

        appUser.setId(null);

        appUserDao.save(appUser);
        return new ResponseEntity<>(appUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Optional<AppUser> optionalAppUser = appUserDao.findById(id);

        if(optionalAppUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        appUserDao.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PutMapping("/user/{id}")
    public ResponseEntity<Void> update(
            @PathVariable int id,
            @RequestBody AppUser appUser
    ) {
        appUser.setId(id);

        Optional<AppUser> optionalAppUser = appUserDao.findById(id);

        if(optionalAppUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        appUserDao.save(appUser);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
