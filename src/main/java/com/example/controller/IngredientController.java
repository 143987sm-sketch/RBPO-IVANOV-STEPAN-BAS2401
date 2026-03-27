package com.example.controller;

import com.example.model.Ingredient;
import com.example.service.IngredientService;
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
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ingredient create(@RequestBody Ingredient body) {
        if (body.getName() == null || body.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }
        if (ingredientService.existsByName(body.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ingredient already exists");
        }
        return ingredientService.create(body);
    }

    @GetMapping
    public List<Ingredient> getAll() {
        return ingredientService.getAll();
    }

    @GetMapping("/{id}")
    public Ingredient getById(@PathVariable Long id) {
        return ingredientService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ingredient not found"));
    }

    @PutMapping("/{id}")
    public Ingredient update(@PathVariable Long id, @RequestBody Ingredient body) {
        if (body.getName() == null || body.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }
        Ingredient existing = ingredientService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ingredient not found"));
        if (!existing.getName().equals(body.getName()) && ingredientService.existsByName(body.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ingredient already exists");
        }
        Ingredient updated = ingredientService.update(id, body.getName());
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ingredient not found");
        }
        return updated;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!ingredientService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ingredient not found");
        }
    }
}
