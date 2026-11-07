package com.example.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class EditionIssue {

    private Long id;
    private LocalDate issueDate;
    private List<Long> articleIds;
    private EditionIssueStatus status;

    private EditionIssue() {
    }

    public static EditionIssue newInstance(
            Long id,
            LocalDate issueDate,
            List<Long> articleIds,
            EditionIssueStatus status
    ) {
        EditionIssue issue = new EditionIssue();
        issue.id = id;
        issue.issueDate = issueDate;
        issue.articleIds = new ArrayList<>(articleIds);
        issue.status = status;
        return issue;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public List<Long> getArticleIds() {
        return Collections.unmodifiableList(articleIds);
    }

    public EditionIssueStatus getStatus() {
        return status;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public void setArticleIds(List<Long> articleIds) {
        this.articleIds = new ArrayList<>(articleIds);
    }

    public void setStatus(EditionIssueStatus status) {
        this.status = status;
    }
}
