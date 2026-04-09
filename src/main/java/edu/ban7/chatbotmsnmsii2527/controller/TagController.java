package edu.ban7.chatbotmsnmsii2527.controller;

import edu.ban7.chatbotmsnmsii2527.dao.TagDao;
import edu.ban7.chatbotmsnmsii2527.model.Tag;
import edu.ban7.chatbotmsnmsii2527.security.IsAdmin;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@IsAdmin
public class TagController {

    protected final TagDao tagDao;

    @GetMapping("/tag/list")
    public List<Tag> getAll() {
        return tagDao.findAll();
    }

    @GetMapping("/tag/{id}")
    public ResponseEntity<Tag> get(@PathVariable int id) {
        Optional<Tag> optionalTag = tagDao.findById(id);

        if(optionalTag.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalTag.get(), HttpStatus.OK);

    }

    @PostMapping("/tag")
    public ResponseEntity<Tag> create(@RequestBody @Valid Tag tag) {

        tag.setId(null);

        tagDao.save(tag);
        return new ResponseEntity<>(tag, HttpStatus.CREATED);
    }

    @DeleteMapping("/tag/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Optional<Tag> optionalTag = tagDao.findById(id);

        if(optionalTag.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        tagDao.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PutMapping("/tag/{id}")
    public ResponseEntity<Void> update(
            @PathVariable int id,
            @RequestBody Tag tag
    ) {
        tag.setId(id);

        Optional<Tag> optionalTag = tagDao.findById(id);

        if(optionalTag.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        tagDao.save(tag);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
