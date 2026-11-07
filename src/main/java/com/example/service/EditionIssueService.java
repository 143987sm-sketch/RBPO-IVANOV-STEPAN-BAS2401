package com.example.service;

import com.example.annotation.Cumponent;
import com.example.facade.EditionIssueArticlesFacade;
import com.example.facade.EditionIssueCreateRequest;
import com.example.facade.EditionIssueScheduleFacade;
import com.example.factory.EditionIssueFactory;
import com.example.model.EditionIssue;
import com.example.model.EditionIssueStatus;
import com.example.repository.ArticleRepository;
import com.example.repository.EditionIssueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Cumponent
@Service
public class EditionIssueService {

    private final EditionIssueRepository issueRepository;
    private final EditionIssueFactory issueFactory;
    private final ArticleRepository articleRepository;

    public EditionIssueService(
            EditionIssueRepository issueRepository,
            EditionIssueFactory issueFactory,
            ArticleRepository articleRepository
    ) {
        this.issueRepository = issueRepository;
        this.issueFactory = issueFactory;
        this.articleRepository = articleRepository;
    }

    public EditionIssue create(EditionIssueCreateRequest request) {
        validateArticles(request.articles());
        EditionIssue draft = issueFactory.create(0L, request.schedule(), request.articles());
        return issueRepository.insert(draft);
    }

    public List<EditionIssue> findAll() {
        return issueRepository.findAll();
    }

    public Optional<EditionIssue> findById(Long id) {
        return issueRepository.findById(id);
    }

    public Optional<EditionIssue> updateSchedule(Long id, EditionIssueScheduleFacade schedule) {
        return issueRepository.findById(id).map(issue -> {
            issue.setIssueDate(schedule.issueDate());
            if (schedule.statusLabel() != null && !schedule.statusLabel().isBlank()) {
                issue.setStatus(EditionIssueStatus.valueOf(schedule.statusLabel()));
            }
            return issue;
        });
    }

    public Optional<EditionIssue> updateArticles(Long id, EditionIssueArticlesFacade articles) {
        validateArticles(articles);
        return issueRepository.findById(id).map(issue -> {
            issue.setArticleIds(issueFactory.mergeArticleIds(articles));
            return issue;
        });
    }

    public Optional<EditionIssue> patch(Long id, com.example.facade.EditionIssuePatchRequest request) {
        Optional<EditionIssue> issue = issueRepository.findById(id);
        if (issue.isEmpty()) {
            return Optional.empty();
        }
        if (request.schedule() != null) {
            issue = updateSchedule(id, request.schedule());
        }
        if (request.articles() != null) {
            issue = updateArticles(id, request.articles());
        }
        return issue;
    }

    public boolean delete(Long id) {
        return issueRepository.deleteById(id);
    }

    private void validateArticles(EditionIssueArticlesFacade articles) {
        for (Long articleId : issueFactory.mergeArticleIds(articles)) {
            if (!articleRepository.existsById(articleId)) {
                throw new IllegalArgumentException("article not found: " + articleId);
            }
        }
    }
}
