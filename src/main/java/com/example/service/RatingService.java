package com.example.service;

import com.example.model.Rating;
import com.example.model.Recipe;
import com.example.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Rating create(Integer value, Recipe recipe, User user) {
        Rating rating = new Rating();
        rating.setValue(value);
        rating.setRecipe(recipe);
        rating.setUser(user);
        em.persist(rating);
        return rating;
    }

    public List<Rating> getAll() {
        return em.createQuery("select r from Rating r", Rating.class).getResultList();
    }

    public Optional<Rating> getById(Long id) {
        return Optional.ofNullable(em.find(Rating.class, id));
    }

    public boolean existsForRecipeAndUser(Long recipeId, Long userId) {
        Long count = em.createQuery(
                        "select count(r) from Rating r where r.recipe.id = :recipeId and r.user.id = :userId",
                        Long.class
                )
                .setParameter("recipeId", recipeId)
                .setParameter("userId", userId)
                .getSingleResult();
        return count != null && count > 0;
    }

    @Transactional
    public Rating setForRecipeAndUser(Integer value, Recipe recipe, User user) {
        List<Rating> existing = em.createQuery(
                        "select r from Rating r where r.recipe.id = :recipeId and r.user.id = :userId",
                        Rating.class
                )
                .setParameter("recipeId", recipe.getId())
                .setParameter("userId", user.getId())
                .setMaxResults(1)
                .getResultList();

        if (existing.isEmpty()) {
            return create(value, recipe, user);
        }
        Rating rating = existing.getFirst();
        rating.setValue(value);
        rating.setRecipe(recipe);
        rating.setUser(user);
        return rating;
    }

    @Transactional
    public Rating update(Long id, Integer value, Recipe recipe, User user) {
        Rating existing = em.find(Rating.class, id);
        if (existing == null) {
            return null;
        }
        existing.setValue(value);
        existing.setRecipe(recipe);
        existing.setUser(user);
        return existing;
    }

    @Transactional
    public boolean delete(Long id) {
        Rating existing = em.find(Rating.class, id);
        if (existing == null) {
            return false;
        }
        em.remove(existing);
        return true;
    }
}
