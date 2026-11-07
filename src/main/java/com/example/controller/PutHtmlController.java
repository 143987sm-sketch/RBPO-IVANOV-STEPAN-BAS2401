package com.example.controller;

import com.example.annotation.Cumponent;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Cumponent
@RestController
public class PutHtmlController {

    private static final String STATIC_HTML = """
            <!DOCTYPE html>
            <html lang="ru">
            <head><meta charset="UTF-8"><title>PUT не поддерживается</title></head>
            <body><h1>Используйте PATCH для обновления ресурсов</h1></body>
            </html>
            """;

    @PutMapping(value = "/**", produces = MediaType.TEXT_HTML_VALUE)
    public String handlePut() {
        return STATIC_HTML;
    }
}
