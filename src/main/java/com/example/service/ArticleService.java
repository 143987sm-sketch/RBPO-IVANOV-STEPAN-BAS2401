package com.example.service;

import com.example.annotation.Cumponent;
import com.example.facade.ArticleContentFacade;
import com.example.facade.ArticleCreateRequest;
import com.example.facade.ArticleLinkFacade;
import com.example.facade.ArticleStatusFacade;
import com.example.factory.ArticleFactory;
import com.example.model.Article;
import com.example.repository.ArticleRepository;
import com.example.repository.EditorialReviewRepository;
import com.example.repository.EditionIssueRepository;
import com.example.repository.EditorialSectionRepository;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Cumponent
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleFactory articleFactory;
    private final EditorialSectionRepository sectionRepository;
    private final UserRepository userRepository;
    private final EditorialReviewRepository reviewRepository;
    private final EditionIssueRepository editionIssueRepository;

    public ArticleService(
            ArticleRepository articleRepository,
            ArticleFactory articleFactory,
            EditorialSectionRepository sectionRepository,
            UserRepository userRepository,
            EditorialReviewRepository reviewRepository,
            EditionIssueRepository editionIssueRepository
    ) {
        this.articleRepository = articleRepository;
        this.articleFactory = articleFactory;
        this.sectionRepository = sectionRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.editionIssueRepository = editionIssueRepository;
    }

    public Article create(ArticleCreateRequest request) {
        validateLinks(request.link());
        Article draft = articleFactory.create(0L, request.link(), request.content());
        return articleRepository.insert(draft);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    public Optional<Article> updateContent(Long id, ArticleContentFacade content) {
        return articleRepository.findById(id).map(article -> {
            article.setTitle(content.title());
            article.setText(content.text());
            return article;
        });
    }

    public Optional<Article> updateLink(Long id, ArticleLinkFacade link) {
        validateLinks(link);
        return articleRepository.findById(id).map(article -> {
            article.setSectionId(link.sectionId());
            article.setAuthorId(link.authorId());
            return article;
        });
    }

    public Optional<Article> updateStatus(Long id, ArticleStatusFacade statusFacade) {
        return articleRepository.findById(id).map(article -> {
            if (statusFacade != null && statusFacade.status() != null) {
                article.setStatus(statusFacade.status());
            }
            if (statusFacade != null && statusFacade.title() != null && !statusFacade.title().isBlank()) {
                article.setTitle(statusFacade.title());
            }
            return article;
        });
    }

    public Optional<Article> patch(Long id, com.example.facade.ArticlePatchRequest request) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isEmpty()) {
            return Optional.empty();
        }
        if (request.link() != null) {
            article = updateLink(id, request.link());
        }
        if (request.content() != null) {
            article = updateContent(id, request.content());
        }
        if (request.status() != null) {
            article = updateStatus(id, request.status());
        }
        return article;
    }

    public boolean delete(Long id) {
        if (!articleRepository.existsById(id)) {
            return false;
        }
        reviewRepository.deleteByArticleId(id);
        editionIssueRepository.removeArticleIdFromAllIssues(id);
        return articleRepository.deleteById(id);
    }

    private void validateLinks(ArticleLinkFacade link) {
        if (!sectionRepository.existsById(link.sectionId())) {
            throw new IllegalArgumentException("section not found: " + link.sectionId());
        }
        if (!userRepository.existsById(link.authorId())) {
            throw new IllegalArgumentException("author not found: " + link.authorId());
        }
    }
}
