package com.example.service;

import com.example.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public User create(User user) {
        user.setId(null);
        em.persist(user);
        return user;
    }

    public List<User> getAll() {
        return em.createQuery("select u from User u", User.class).getResultList();
    }

    public Optional<User> getById(Long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    public boolean existsByUsername(String username) {
        Long count = em.createQuery("select count(u) from User u where u.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
        return count != null && count > 0;
    }

    @Transactional
    public User update(Long id, String username) {
        User existing = em.find(User.class, id);
        if (existing == null) {
            return null;
        }
        existing.setUsername(username);
        return existing;
    }

    @Transactional
    public boolean delete(Long id) {
        User existing = em.find(User.class, id);
        if (existing == null) {
            return false;
        }
        em.remove(existing);
        return true;
    }
}
