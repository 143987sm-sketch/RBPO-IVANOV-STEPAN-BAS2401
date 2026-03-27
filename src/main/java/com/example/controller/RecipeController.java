package com.example.controller;

import com.example.model.Ingredient;
import com.example.model.Recipe;
import com.example.model.User;
import com.example.service.IngredientService;
import com.example.service.RecipeService;
import com.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final UserService userService;
    private final IngredientService ingredientService;

    public RecipeController(
            RecipeService recipeService,
            UserService userService,
            IngredientService ingredientService
    ) {
        this.recipeService = recipeService;
        this.userService = userService;
        this.ingredientService = ingredientService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Recipe create(@RequestBody RecipeRequest body) {
        return applyAndCreate(body);
    }

    @GetMapping
    public List<Recipe> getAll() {
        return recipeService.getAll();
    }

    @GetMapping("/{id}")
    public Recipe getById(@PathVariable Long id) {
        return recipeService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe not found"));
    }

    @PutMapping("/{id}")
    public Recipe update(@PathVariable Long id, @RequestBody RecipeRequest body) {
        if (body.title() == null || body.title().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title is required");
        }

        User author = userService.getById(body.authorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "author not found"));
        List<Ingredient> ingredients = resolveIngredients(body.ingredientIds());

        Recipe updated = recipeService.update(id, body.title(), body.description(), author, ingredients);
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe not found");
        }
        return updated;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!recipeService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe not found");
        }
    }

    // Business operation #1: publish recipe by ingredient names (creates missing ingredients)
    @PostMapping("/publish")
    @ResponseStatus(HttpStatus.CREATED)
    public Recipe publish(@RequestBody PublishRecipeRequest body) {
        if (body.title() == null || body.title().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title is required");
        }
        User author = userService.getById(body.authorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "author not found"));

        List<Ingredient> ingredients = new ArrayList<>();
        if (body.ingredientNames() != null) {
            for (String name : body.ingredientNames()) {
                try {
                    ingredients.add(ingredientService.getOrCreateByName(name));
                } catch (IllegalArgumentException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
                }
            }
        }

        return recipeService.create(body.title(), body.description(), author, ingredients);
    }

    // Business operation #2: add ingredient to recipe by name (creates ingredient if needed)
    @PostMapping("/{id}/ingredients")
    public Recipe addIngredient(@PathVariable Long id, @RequestBody AddIngredientRequest body) {
        if (body.name() == null || body.name().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }
        Ingredient ingredient;
        try {
            ingredient = ingredientService.getOrCreateByName(body.name());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        Recipe updated = recipeService.addIngredient(id, ingredient);
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe not found");
        }
        return updated;
    }

    // Business operation #3: recipe details + rating & comment stats
    @GetMapping("/{id}/details")
    public RecipeService.RecipeDetails details(@PathVariable Long id) {
        RecipeService.RecipeDetails details = recipeService.getDetails(id);
        if (details == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe not found");
        }
        return details;
    }

    // Business operation #4: top recipes by rating
    @GetMapping("/top")
    public List<RecipeService.RecipeSummary> top(@RequestParam(defaultValue = "5") int limit) {
        if (limit < 1 || limit > 50) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "limit must be in range 1..50");
        }
        return recipeService.getTop(limit);
    }

    private Recipe applyAndCreate(RecipeRequest body) {
        if (body.title() == null || body.title().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title is required");
        }
        User author = userService.getById(body.authorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "author not found"));
        List<Ingredient> ingredients = resolveIngredients(body.ingredientIds());
        return recipeService.create(body.title(), body.description(), author, ingredients);
    }

    private List<Ingredient> resolveIngredients(List<Long> ingredientIds) {
        List<Ingredient> ingredients = new ArrayList<>();
        if (ingredientIds == null) {
            return ingredients;
        }
        for (Long ingredientId : ingredientIds) {
            Ingredient ingredient = ingredientService.getById(ingredientId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ingredient not found: " + ingredientId));
            ingredients.add(ingredient);
        }
        return ingredients;
    }

    public record RecipeRequest(String title, String description, Long authorId, List<Long> ingredientIds) {
    }

    public record PublishRecipeRequest(String title, String description, Long authorId, List<String> ingredientNames) {
    }

    public record AddIngredientRequest(String name) {
    }
}
