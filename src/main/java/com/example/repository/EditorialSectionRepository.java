package com.example.repository;

import com.example.annotation.Cumponent;
import com.example.model.EditorialSection;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Cumponent
@Repository
public class EditorialSectionRepository {

    private final Map<Long, EditorialSection> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequences = new AtomicLong(0);

    public EditorialSection insert(EditorialSection section) {
        long id = sequences.incrementAndGet();
        EditorialSection stored = EditorialSection.newInstance(
                id,
                section.getName(),
                section.isAvailableForPublication()
        );
        storage.put(id, stored);
        return stored;
    }

    public Optional<EditorialSection> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<EditorialSection> findAll() {
        return new ArrayList<>(storage.values());
    }

    public boolean deleteById(Long id) {
        return storage.remove(id) != null;
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }
}
