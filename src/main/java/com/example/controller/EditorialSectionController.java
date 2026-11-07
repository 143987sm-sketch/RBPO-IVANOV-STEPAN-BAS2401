package com.example.controller;

import com.example.annotation.Cumponent;
import com.example.facade.EditorialSectionFacade;
import com.example.model.EditorialSection;
import com.example.service.EditorialSectionService;
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
@RequestMapping("/api/editorial-sections")
public class EditorialSectionController {

    private final EditorialSectionService sectionService;

    public EditorialSectionController(EditorialSectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping
    public List<EditorialSection> list() {
        return sectionService.findAll();
    }

    @GetMapping("/{id}")
    public EditorialSection get(@PathVariable Long id) {
        return sectionService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "section not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EditorialSection create(@RequestBody EditorialSectionFacade facade) {
        return sectionService.create(facade);
    }

    @PatchMapping("/{id}")
    public EditorialSection patch(@PathVariable Long id, @RequestBody EditorialSectionFacade facade) {
        return sectionService.update(id, facade)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "section not found"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!sectionService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "section not found");
        }
    }
}
