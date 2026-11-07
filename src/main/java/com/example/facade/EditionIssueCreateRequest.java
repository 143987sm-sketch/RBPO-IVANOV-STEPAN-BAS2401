package com.example.facade;

import java.time.LocalDate;

public record EditionIssueCreateRequest(EditionIssueScheduleFacade schedule, EditionIssueArticlesFacade articles) {
}
