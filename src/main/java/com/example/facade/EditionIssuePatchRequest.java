package com.example.facade;

public record EditionIssuePatchRequest(
        EditionIssueScheduleFacade schedule,
        EditionIssueArticlesFacade articles
) {
}
