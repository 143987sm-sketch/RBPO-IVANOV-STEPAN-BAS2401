package com.example.facade;

import com.example.model.ReviewDecision;

public record EditorialReviewCreateRequest(EditorialReviewFacade review, EditorialReviewNotesFacade notes) {
}
