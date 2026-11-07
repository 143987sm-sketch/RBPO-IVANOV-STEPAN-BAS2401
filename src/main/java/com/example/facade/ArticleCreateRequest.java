package com.example.facade;

public record ArticleCreateRequest(ArticleLinkFacade link, ArticleContentFacade content) {
}
