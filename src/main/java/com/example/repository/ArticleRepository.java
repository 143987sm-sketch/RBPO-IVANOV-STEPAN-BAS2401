package com.example.repository;

import com.example.annotation.Cumponent;
import com.example.model.Article;
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
public class ArticleRepository {

    private final Map<Long, Article> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequences = new AtomicLong(0);

    public Article insert(Article article) {
        long id = sequences.incrementAndGet();
        Article stored = Article.newInstance(
                id,
                article.getSectionId(),
                article.getAuthorId(),
                article.getTitle(),
                article.getText(),
                article.getStatus()
        );
        storage.put(id, stored);
        return stored;
    }

    public Optional<Article> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Article> findAll() {
        return new ArrayList<>(storage.values());
    }

    public List<Article> findByAuthorId(Long authorId) {
        return storage.values().stream()
                .filter(article -> authorId.equals(article.getAuthorId()))
                .collect(Collectors.toList());
    }

    public List<Article> findBySectionId(Long sectionId) {
        return storage.values().stream()
                .filter(article -> sectionId.equals(article.getSectionId()))
                .collect(Collectors.toList());
    }

    public boolean deleteById(Long id) {
        return storage.remove(id) != null;
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }
}
