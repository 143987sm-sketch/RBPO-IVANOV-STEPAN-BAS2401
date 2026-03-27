package com.example.controller;

import com.example.model.User;
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
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User body) {
        if (body.getUsername() == null || body.getUsername().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username is required");
        }
        if (userService.existsByUsername(body.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username already exists");
        }
        return userService.create(body);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User body) {
        if (body.getUsername() == null || body.getUsername().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username is required");
        }
        User existing = userService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        if (!existing.getUsername().equals(body.getUsername()) && userService.existsByUsername(body.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username already exists");
        }
        User updated = userService.update(id, body.getUsername());
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
        }
        return updated;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!userService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
        }
    }
}
