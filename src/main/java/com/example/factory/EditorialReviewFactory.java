package com.example.factory;

import com.example.annotation.Cumponent;
import com.example.facade.EditorialReviewFacade;
import com.example.facade.EditorialReviewNotesFacade;
import com.example.model.EditorialReview;

import java.time.Instant;

@Cumponent
public class EditorialReviewFactory {

    public EditorialReview create(Long id, EditorialReviewFacade facade, EditorialReviewNotesFacade notes, Instant reviewedAt) {
        return EditorialReview.newInstance(
                id,
                facade.articleId(),
                facade.decision(),
                notes.remarks(),
                reviewedAt
        );
    }
}
