package com.example.factory;

import com.example.annotation.Cumponent;
import com.example.facade.ArticleContentFacade;
import com.example.facade.ArticleLinkFacade;
import com.example.model.Article;
import com.example.model.ArticleStatus;

@Cumponent
public class ArticleFactory {

    public Article create(Long id, ArticleLinkFacade link, ArticleContentFacade content) {
        return Article.newInstance(
                id,
                link.sectionId(),
                link.authorId(),
                content.title(),
                content.text(),
                ArticleStatus.DRAFT
        );
    }

    public Article create(
            Long id,
            ArticleLinkFacade link,
            ArticleContentFacade content,
            ArticleStatus status
    ) {
        return Article.newInstance(
                id,
                link.sectionId(),
                link.authorId(),
                content.title(),
                content.text(),
                status
        );
    }
}
