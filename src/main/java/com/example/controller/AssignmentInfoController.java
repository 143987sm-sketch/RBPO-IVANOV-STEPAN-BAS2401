package com.example.controller;

import com.example.annotation.Cumponent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Cumponent
@RestController
@RequestMapping("/api/info")
public class AssignmentInfoController {

    @GetMapping("/greeting")
    public String greeting() {
        return "привет это первое задание";
    }

    @GetMapping("/numbers")
    public List<Integer> randomNumbers() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return List.of(
                random.nextInt(1, 100),
                random.nextInt(1, 100),
                random.nextInt(1, 100)
        );
    }
}
