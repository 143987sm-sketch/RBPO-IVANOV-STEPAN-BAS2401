package com.example.controller;

import com.example.annotation.Cumponent;
import com.example.facade.EditorialReviewCreateRequest;
import com.example.facade.EditorialReviewPatchRequest;
import com.example.model.EditorialReview;
import com.example.service.EditorialReviewService;
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
@RequestMapping("/api/editorial-reviews")
public class EditorialReviewController {

    private final EditorialReviewService reviewService;

    public EditorialReviewController(EditorialReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<EditorialReview> list() {
        return reviewService.findAll();
    }

    @GetMapping("/{id}")
    public EditorialReview get(@PathVariable Long id) {
        return reviewService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "review not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EditorialReview create(@RequestBody EditorialReviewCreateRequest request) {
        return reviewService.create(request);
    }

    @PatchMapping("/{id}")
    public EditorialReview patch(@PathVariable Long id, @RequestBody EditorialReviewPatchRequest request) {
        return reviewService.patch(id, request)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "review not found"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!reviewService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "review not found");
        }
    }
}
