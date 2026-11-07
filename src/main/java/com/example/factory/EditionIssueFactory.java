package com.example.factory;

import com.example.annotation.Cumponent;
import com.example.facade.EditionIssueArticlesFacade;
import com.example.facade.EditionIssueScheduleFacade;
import com.example.model.EditionIssue;
import com.example.model.EditionIssueStatus;

import java.util.ArrayList;
import java.util.List;

@Cumponent
public class EditionIssueFactory {

    public EditionIssue create(Long id, EditionIssueScheduleFacade schedule, EditionIssueArticlesFacade articles) {
        return EditionIssue.newInstance(
                id,
                schedule.issueDate(),
                mergeArticleIds(articles),
                EditionIssueStatus.PLANNED
        );
    }

    public EditionIssue create(
            Long id,
            EditionIssueScheduleFacade schedule,
            EditionIssueArticlesFacade articles,
            EditionIssueStatus status
    ) {
        return EditionIssue.newInstance(id, schedule.issueDate(), mergeArticleIds(articles), status);
    }

    public List<Long> mergeArticleIds(EditionIssueArticlesFacade articles) {
        List<Long> ids = new ArrayList<>();
        if (articles.primaryArticleId() != null && !articles.primaryArticleId().isBlank()) {
            ids.add(Long.valueOf(articles.primaryArticleId().trim()));
        }
        if (articles.additionalArticleIds() != null && !articles.additionalArticleIds().isBlank()) {
            for (String part : articles.additionalArticleIds().split(",")) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) {
                    ids.add(Long.valueOf(trimmed));
                }
            }
        }
        return ids;
    }
}
