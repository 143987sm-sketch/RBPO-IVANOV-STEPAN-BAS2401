package com.example.repository;

import com.example.annotation.Cumponent;
import com.example.model.EditorialReview;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Cumponent
@Repository
public class EditorialReviewRepository {

    private final Map<Long, EditorialReview> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequences = new AtomicLong(0);

    public EditorialReview insert(EditorialReview review) {
        long id = sequences.incrementAndGet();
        EditorialReview stored = EditorialReview.newInstance(
                id,
                review.getArticleId(),
                review.getDecision(),
                review.getRemarks(),
                review.getReviewedAt()
        );
        storage.put(id, stored);
        return stored;
    }

    public Optional<EditorialReview> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<EditorialReview> findAll() {
        return new ArrayList<>(storage.values());
    }

    public List<EditorialReview> findByArticleId(Long articleId) {
        return storage.values().stream()
                .filter(review -> articleId.equals(review.getArticleId()))
                .collect(Collectors.toList());
    }

    public boolean deleteById(Long id) {
        return storage.remove(id) != null;
    }

    public void deleteByArticleId(Long articleId) {
        storage.entrySet().removeIf(entry -> articleId.equals(entry.getValue().getArticleId()));
    }
}
