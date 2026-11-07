package com.example.facade;

public record EditorialReviewPatchRequest(
        EditorialReviewFacade review,
        EditorialReviewNotesFacade notes
) {
}
