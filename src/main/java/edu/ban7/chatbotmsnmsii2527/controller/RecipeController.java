package edu.ban7.chatbotmsnmsii2527.controller;

import edu.ban7.chatbotmsnmsii2527.dao.RecipeDao;
import edu.ban7.chatbotmsnmsii2527.model.Recipe;
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
public class RecipeController {

    protected final RecipeDao recipeDao;

    @GetMapping("/recipe/list")
    public List<Recipe> getAll() {
        return recipeDao.findAll();
    }

    @GetMapping("/recipe/{id}")
    public ResponseEntity<Recipe> get(@PathVariable int id) {
        Optional<Recipe> optionalRecipe = recipeDao.findById(id);

        if(optionalRecipe.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalRecipe.get(), HttpStatus.OK);
    }

    @GetMapping("/filter/exclude-tags")
    public ResponseEntity<List<Recipe>> getExcludingTags(
            @RequestParam List<Integer> tagIds) {

        if (tagIds.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<Recipe> recipes = recipeDao.findExcludingAnyTag(tagIds);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/filter/all-tags")
    public ResponseEntity<List<Recipe>> getByAllTags(
            @RequestParam List<Integer> tagIds) {

        if (tagIds.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<Recipe> recipes = recipeDao.findByAllTags(tagIds, tagIds.size());
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/filter/any-tag")
    public ResponseEntity<List<Recipe>> getByAnyTag(
            @RequestParam List<Integer> tagIds) {

        if (tagIds.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<Recipe> recipes = recipeDao.findByAnyTag(tagIds);
        return ResponseEntity.ok(recipes);
    }

    @PostMapping("/recipe")
    public ResponseEntity<Recipe> create(@RequestBody @Valid Recipe recipe) {

        recipe.setId(null);

        recipeDao.save(recipe);
        return new ResponseEntity<>(recipe, HttpStatus.CREATED);
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Optional<Recipe> optionalRecipe = recipeDao.findById(id);

        if(optionalRecipe.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        recipeDao.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PutMapping("/recipe/{id}")
    public ResponseEntity<Void> update(
            @PathVariable int id,
            @RequestBody Recipe recipe
    ) {
        recipe.setId(id);

        Optional<Recipe> optionalRecipe = recipeDao.findById(id);

        if(optionalRecipe.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        recipeDao.save(recipe);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
