package com.example.demo.controller;

import com.example.demo.User;
import com.example.demo.service.RAGService;
import com.example.demo.service.UserService;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rag")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RAGController {
    @Autowired
    private final RAGService ragService;

    public RAGController(RAGService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/query")
    public ResponseEntity<Map<String, String>> processQuery(@RequestBody Map<String, String> request) {
        String query = request.get("query");
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Query is required"));
        }


        String response = ragService.processQuery(query, Long.valueOf(request.get("customerId")));
        return ResponseEntity.ok(Collections.singletonMap("response", response));
    }
    @Autowired
    private UserService userService;

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }
}
