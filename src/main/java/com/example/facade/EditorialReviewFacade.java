package com.example.facade;

import com.example.model.ReviewDecision;

public record EditorialReviewFacade(Long articleId, ReviewDecision decision) {
}
