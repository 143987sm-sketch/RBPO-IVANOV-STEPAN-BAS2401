package com.example.repository;

import com.example.annotation.Cumponent;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Cumponent
@Repository
public class UserRepository {

    private final Map<Long, com.example.model.User> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequences = new AtomicLong(0);

    public com.example.model.User save(com.example.model.User user) {
        storage.put(user.getId(), user);
        return user;
    }

    public com.example.model.User insert(com.example.model.User user) {
        long id = sequences.incrementAndGet();
        com.example.model.User stored = com.example.model.User.newInstance(
                id,
                user.getUsername(),
                user.getDisplayName()
        );
        storage.put(id, stored);
        return stored;
    }

    public Optional<com.example.model.User> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<com.example.model.User> findAll() {
        return new ArrayList<>(storage.values());
    }

    public boolean deleteById(Long id) {
        return storage.remove(id) != null;
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }
}
