package com.example.service;

import com.example.model.Ingredient;
import com.example.model.Recipe;
import com.example.model.User;
import jakarta.persistence.Tuple;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
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
    public Recipe addIngredient(Long recipeId, Ingredient ingredient) {
        Recipe recipe = em.find(Recipe.class, recipeId);
        if (recipe == null) {
            return null;
        }
        List<Ingredient> list = recipe.getIngredients();
        boolean exists = list.stream().anyMatch(i -> i.getId().equals(ingredient.getId()));
        if (!exists) {
            list.add(ingredient);
        }
        return recipe;
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

    public RecipeDetails getDetails(Long recipeId) {
        Recipe recipe = em.find(Recipe.class, recipeId);
        if (recipe == null) {
            return null;
        }

        Tuple stats = em.createQuery(
                        "select avg(r.value) as avgRating, count(r) as ratingsCount from Rating r where r.recipe.id = :rid",
                        Tuple.class
                )
                .setParameter("rid", recipeId)
                .getSingleResult();

        Long commentsCount = em.createQuery(
                        "select count(c) from Comment c where c.recipe.id = :rid",
                        Long.class
                )
                .setParameter("rid", recipeId)
                .getSingleResult();

        Double avgRating = stats.get("avgRating", Double.class);
        Long ratingsCount = stats.get("ratingsCount", Long.class);

        List<String> ingredientNames = recipe.getIngredients().stream()
                .map(Ingredient::getName)
                .sorted(Comparator.naturalOrder())
                .toList();

        return new RecipeDetails(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getAuthor().getId(),
                ingredientNames,
                avgRating == null ? 0.0 : avgRating,
                ratingsCount == null ? 0 : ratingsCount,
                commentsCount == null ? 0 : commentsCount
        );
    }

    public List<RecipeSummary> getTop(int limit) {
        List<Tuple> tuples = em.createQuery(
                        "select r.id as id, r.title as title, avg(rt.value) as avgRating, count(rt) as ratingsCount " +
                                "from Recipe r left join Rating rt on rt.recipe = r " +
                                "group by r.id, r.title " +
                                "order by avg(rt.value) desc nulls last, count(rt) desc, r.id asc",
                        Tuple.class
                )
                .setMaxResults(Math.max(1, limit))
                .getResultList();

        List<RecipeSummary> out = new ArrayList<>();
        for (Tuple t : tuples) {
            Double avg = t.get("avgRating", Double.class);
            Long cnt = t.get("ratingsCount", Long.class);
            out.add(new RecipeSummary(
                    t.get("id", Long.class),
                    t.get("title", String.class),
                    avg == null ? 0.0 : avg,
                    cnt == null ? 0 : cnt
            ));
        }
        return out;
    }

    public record RecipeDetails(
            Long id,
            String title,
            String description,
            Long authorId,
            List<String> ingredients,
            double averageRating,
            long ratingsCount,
            long commentsCount
    ) {
    }

    public record RecipeSummary(Long id, String title, double averageRating, long ratingsCount) {
    }
}
