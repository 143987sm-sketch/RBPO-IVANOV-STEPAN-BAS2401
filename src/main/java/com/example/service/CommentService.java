package com.example.service;

import com.example.model.Comment;
import com.example.model.Recipe;
import com.example.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Comment create(String text, Recipe recipe, User user) {
        Comment comment = new Comment();
        comment.setText(text);
        comment.setRecipe(recipe);
        comment.setUser(user);
        em.persist(comment);
        return comment;
    }

    public List<Comment> getAll() {
        return em.createQuery("select c from Comment c", Comment.class).getResultList();
    }

    public Optional<Comment> getById(Long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Transactional
    public Comment update(Long id, String text, Recipe recipe, User user) {
        Comment existing = em.find(Comment.class, id);
        if (existing == null) {
            return null;
        }
        existing.setText(text);
        existing.setRecipe(recipe);
        existing.setUser(user);
        return existing;
    }

    @Transactional
    public boolean delete(Long id) {
        Comment existing = em.find(Comment.class, id);
        if (existing == null) {
            return false;
        }
        em.remove(existing);
        return true;
    }
}
