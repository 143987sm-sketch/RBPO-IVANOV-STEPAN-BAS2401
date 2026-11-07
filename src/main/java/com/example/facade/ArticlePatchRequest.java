package com.example.facade;

public record ArticlePatchRequest(
        ArticleLinkFacade link,
        ArticleContentFacade content,
        ArticleStatusFacade status
) {
}
