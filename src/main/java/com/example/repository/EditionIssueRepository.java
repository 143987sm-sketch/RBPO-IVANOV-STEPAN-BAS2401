package com.example.repository;

import com.example.annotation.Cumponent;
import com.example.model.EditionIssue;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Cumponent
@Repository
public class EditionIssueRepository {

    private final Map<Long, EditionIssue> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequences = new AtomicLong(0);

    public EditionIssue insert(EditionIssue issue) {
        long id = sequences.incrementAndGet();
        EditionIssue stored = EditionIssue.newInstance(
                id,
                issue.getIssueDate(),
                issue.getArticleIds(),
                issue.getStatus()
        );
        storage.put(id, stored);
        return stored;
    }

    public Optional<EditionIssue> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<EditionIssue> findAll() {
        return new ArrayList<>(storage.values());
    }

    public boolean deleteById(Long id) {
        return storage.remove(id) != null;
    }

    public void removeArticleIdFromAllIssues(Long articleId) {
        for (EditionIssue issue : storage.values()) {
            List<Long> ids = new ArrayList<>(issue.getArticleIds());
            if (ids.remove(articleId)) {
                issue.setArticleIds(ids);
            }
        }
    }
}
