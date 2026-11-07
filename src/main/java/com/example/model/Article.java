package com.example.model;

public final class Article {

    private Long id;
    private Long sectionId;
    private Long authorId;
    private String title;
    private String text;
    private ArticleStatus status;

    private Article() {
    }

    public static Article newInstance(
            Long id,
            Long sectionId,
            Long authorId,
            String title,
            String text,
            ArticleStatus status
    ) {
        Article article = new Article();
        article.id = id;
        article.sectionId = sectionId;
        article.authorId = authorId;
        article.title = title;
        article.text = text;
        article.status = status;
        return article;
    }

    public Long getId() {
        return id;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public ArticleStatus getStatus() {
        return status;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setStatus(ArticleStatus status) {
        this.status = status;
    }
}
