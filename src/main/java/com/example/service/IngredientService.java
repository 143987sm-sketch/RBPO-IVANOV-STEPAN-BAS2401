package com.example.service;

import com.example.model.Ingredient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Ingredient create(Ingredient ingredient) {
        ingredient.setId(null);
        em.persist(ingredient);
        return ingredient;
    }

    public List<Ingredient> getAll() {
        return em.createQuery("select i from Ingredient i", Ingredient.class).getResultList();
    }

    public Optional<Ingredient> getById(Long id) {
        return Optional.ofNullable(em.find(Ingredient.class, id));
    }

    public boolean existsByName(String name) {
        Long count = em.createQuery("select count(i) from Ingredient i where i.name = :name", Long.class)
                .setParameter("name", name)
                .getSingleResult();
        return count != null && count > 0;
    }

    @Transactional
    public Ingredient getOrCreateByName(String rawName) {
        String name = normalizeName(rawName);
        if (name.isBlank()) {
            throw new IllegalArgumentException("ingredient name is blank");
        }
        List<Ingredient> found = em.createQuery("select i from Ingredient i where i.name = :name", Ingredient.class)
                .setParameter("name", name)
                .setMaxResults(1)
                .getResultList();
        if (!found.isEmpty()) {
            return found.getFirst();
        }

        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        em.persist(ingredient);
        return ingredient;
    }

    @Transactional
    public Ingredient update(Long id, String name) {
        Ingredient existing = em.find(Ingredient.class, id);
        if (existing == null) {
            return null;
        }
        existing.setName(name);
        return existing;
    }

    @Transactional
    public boolean delete(Long id) {
        Ingredient existing = em.find(Ingredient.class, id);
        if (existing == null) {
            return false;
        }
        em.remove(existing);
        return true;
    }

    private String normalizeName(String raw) {
        if (raw == null) {
            return "";
        }
        return raw.trim().replaceAll("\\s+", " ");
    }
}
