package com.example.service;

import com.example.annotation.Cumponent;
import com.example.facade.EditorialReviewCreateRequest;
import com.example.facade.EditorialReviewFacade;
import com.example.facade.EditorialReviewNotesFacade;
import com.example.factory.EditorialReviewFactory;
import com.example.model.EditorialReview;
import com.example.repository.ArticleRepository;
import com.example.repository.EditorialReviewRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Cumponent
@Service
public class EditorialReviewService {

    private final EditorialReviewRepository reviewRepository;
    private final EditorialReviewFactory reviewFactory;
    private final ArticleRepository articleRepository;
    private final Clock clock;

    public EditorialReviewService(
            EditorialReviewRepository reviewRepository,
            EditorialReviewFactory reviewFactory,
            ArticleRepository articleRepository,
            Clock clock
    ) {
        this.reviewRepository = reviewRepository;
        this.reviewFactory = reviewFactory;
        this.articleRepository = articleRepository;
        this.clock = clock;
    }

    public EditorialReview create(EditorialReviewCreateRequest request) {
        if (!articleRepository.existsById(request.review().articleId())) {
            throw new IllegalArgumentException("article not found: " + request.review().articleId());
        }
        Instant reviewedAt = clock.instant();
        EditorialReview draft = reviewFactory.create(0L, request.review(), request.notes(), reviewedAt);
        return reviewRepository.insert(draft);
    }

    public List<EditorialReview> findAll() {
        return reviewRepository.findAll();
    }

    public Optional<EditorialReview> findById(Long id) {
        return reviewRepository.findById(id);
    }

    public Optional<EditorialReview> updateDecision(Long id, EditorialReviewFacade facade) {
        if (!articleRepository.existsById(facade.articleId())) {
            throw new IllegalArgumentException("article not found: " + facade.articleId());
        }
        return reviewRepository.findById(id).map(review -> {
            review.setArticleId(facade.articleId());
            review.setDecision(facade.decision());
            review.setReviewedAt(clock.instant());
            return review;
        });
    }

    public Optional<EditorialReview> updateNotes(Long id, EditorialReviewNotesFacade notes) {
        return reviewRepository.findById(id).map(review -> {
            review.setRemarks(notes.remarks());
            review.setReviewedAt(clock.instant());
            return review;
        });
    }

    public boolean delete(Long id) {
        return reviewRepository.deleteById(id);
    }

    public Optional<EditorialReview> patch(Long id, com.example.facade.EditorialReviewPatchRequest request) {
        Optional<EditorialReview> review = reviewRepository.findById(id);
        if (review.isEmpty()) {
            return Optional.empty();
        }
        if (request.review() != null) {
            review = updateDecision(id, request.review());
        }
        if (request.notes() != null) {
            review = updateNotes(id, request.notes());
        }
        return review;
    }
}
