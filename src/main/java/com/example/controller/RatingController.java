package com.example.controller;

import com.example.model.Rating;
import com.example.model.Recipe;
import com.example.model.User;
import com.example.service.RatingService;
import com.example.service.RecipeService;
import com.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;
    private final RecipeService recipeService;
    private final UserService userService;

    public RatingController(
            RatingService ratingService,
            RecipeService recipeService,
            UserService userService
    ) {
        this.ratingService = ratingService;
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Rating create(@RequestBody RatingRequest body) {
        if (body.value() == null || body.value() < 1 || body.value() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "value must be in range 1..5");
        }
        if (ratingService.existsForRecipeAndUser(body.recipeId(), body.userId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "rating for this recipe/user already exists");
        }
        Recipe recipe = recipeService.getById(body.recipeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe not found"));
        User user = userService.getById(body.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        return ratingService.create(body.value(), recipe, user);
    }

    @GetMapping
    public List<Rating> getAll() {
        return ratingService.getAll();
    }

    @GetMapping("/{id}")
    public Rating getById(@PathVariable Long id) {
        return ratingService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "rating not found"));
    }

    @PutMapping("/{id}")
    public Rating update(@PathVariable Long id, @RequestBody RatingRequest body) {
        if (body.value() == null || body.value() < 1 || body.value() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "value must be in range 1..5");
        }
        Recipe recipe = recipeService.getById(body.recipeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe not found"));
        User user = userService.getById(body.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        Rating updated = ratingService.update(id, body.value(), recipe, user);
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "rating not found");
        }
        return updated;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!ratingService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "rating not found");
        }
    }

    public record RatingRequest(Integer value, Long recipeId, Long userId) {
    }
}
