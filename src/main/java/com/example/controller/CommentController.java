package com.example.controller;

import com.example.model.Comment;
import com.example.model.Recipe;
import com.example.model.User;
import com.example.service.CommentService;
import com.example.service.RecipeService;
import com.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final RecipeService recipeService;
    private final UserService userService;

    public CommentController(
            CommentService commentService,
            RecipeService recipeService,
            UserService userService
    ) {
        this.commentService = commentService;
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Comment create(@RequestBody CommentRequest body) {
        if (body.text() == null || body.text().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "text is required");
        }
        Recipe recipe = recipeService.getById(body.recipeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe not found"));
        User user = userService.getById(body.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        return commentService.create(body.text(), recipe, user);
    }

    @GetMapping
    public List<Comment> getAll() {
        return commentService.getAll();
    }

    @GetMapping("/{id}")
    public Comment getById(@PathVariable Long id) {
        return commentService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "comment not found"));
    }

    @PutMapping("/{id}")
    public Comment update(@PathVariable Long id, @RequestBody CommentRequest body) {
        if (body.text() == null || body.text().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "text is required");
        }
        Recipe recipe = recipeService.getById(body.recipeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe not found"));
        User user = userService.getById(body.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        Comment updated = commentService.update(id, body.text(), recipe, user);
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "comment not found");
        }
        return updated;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!commentService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "comment not found");
        }
    }

    public record CommentRequest(String text, Long recipeId, Long userId) {
    }
}
