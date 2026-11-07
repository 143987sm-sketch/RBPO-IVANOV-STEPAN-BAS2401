package com.example.controller;

import com.example.annotation.Cumponent;
import com.example.facade.ArticleCreateRequest;
import com.example.facade.ArticlePatchRequest;
import com.example.model.Article;
import com.example.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Cumponent
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<Article> list() {
        return articleService.findAll();
    }

    @GetMapping("/{id}")
    public Article get(@PathVariable Long id) {
        return articleService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "article not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Article create(@RequestBody ArticleCreateRequest request) {
        return articleService.create(request);
    }

    @PatchMapping("/{id}")
    public Article patch(@PathVariable Long id, @RequestBody ArticlePatchRequest request) {
        return articleService.patch(id, request)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "article not found"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!articleService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "article not found");
        }
    }
}
