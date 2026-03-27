package com.example.service;

import com.example.model.Ingredient;
import com.example.model.Recipe;
import com.example.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Recipe create(String title, String description, User author, List<Ingredient> ingredients) {
        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        recipe.setDescription(description);
        recipe.setAuthor(author);
        recipe.setIngredients(ingredients);
        em.persist(recipe);
        return recipe;
    }

    public List<Recipe> getAll() {
        return em.createQuery("select r from Recipe r", Recipe.class).getResultList();
    }

    public Optional<Recipe> getById(Long id) {
        return Optional.ofNullable(em.find(Recipe.class, id));
    }

    @Transactional
    public Recipe update(Long id, String title, String description, User author, List<Ingredient> ingredients) {
        Recipe existing = em.find(Recipe.class, id);
        if (existing == null) {
            return null;
        }
        existing.setTitle(title);
        existing.setDescription(description);
        existing.setAuthor(author);
        existing.setIngredients(ingredients);
        return existing;
    }

    @Transactional
    public boolean delete(Long id) {
        Recipe existing = em.find(Recipe.class, id);
        if (existing == null) {
            return false;
        }
        em.remove(existing);
        return true;
    }
}
