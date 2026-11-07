package com.example.model;

import java.time.Instant;

public final class EditorialReview {

    private Long id;
    private Long articleId;
    private ReviewDecision decision;
    private String remarks;
    private Instant reviewedAt;

    private EditorialReview() {
    }

    public static EditorialReview newInstance(
            Long id,
            Long articleId,
            ReviewDecision decision,
            String remarks,
            Instant reviewedAt
    ) {
        EditorialReview review = new EditorialReview();
        review.id = id;
        review.articleId = articleId;
        review.decision = decision;
        review.remarks = remarks;
        review.reviewedAt = reviewedAt;
        return review;
    }

    public Long getId() {
        return id;
    }

    public Long getArticleId() {
        return articleId;
    }

    public ReviewDecision getDecision() {
        return decision;
    }

    public String getRemarks() {
        return remarks;
    }

    public Instant getReviewedAt() {
        return reviewedAt;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public void setDecision(ReviewDecision decision) {
        this.decision = decision;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setReviewedAt(Instant reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
}
