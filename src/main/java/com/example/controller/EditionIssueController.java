package com.example.controller;

import com.example.annotation.Cumponent;
import com.example.facade.EditionIssueCreateRequest;
import com.example.facade.EditionIssuePatchRequest;
import com.example.model.EditionIssue;
import com.example.service.EditionIssueService;
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
@RequestMapping("/api/edition-issues")
public class EditionIssueController {

    private final EditionIssueService issueService;

    public EditionIssueController(EditionIssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping
    public List<EditionIssue> list() {
        return issueService.findAll();
    }

    @GetMapping("/{id}")
    public EditionIssue get(@PathVariable Long id) {
        return issueService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "edition issue not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EditionIssue create(@RequestBody EditionIssueCreateRequest request) {
        return issueService.create(request);
    }

    @PatchMapping("/{id}")
    public EditionIssue patch(@PathVariable Long id, @RequestBody EditionIssuePatchRequest request) {
        return issueService.patch(id, request)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "edition issue not found"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!issueService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "edition issue not found");
        }
    }
}
