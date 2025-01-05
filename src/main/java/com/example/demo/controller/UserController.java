package com.example.demo.controller;

import com.example.demo.User;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/created-after")
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<User>> getUsersCreatedAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<User> users = userService.findUsersCreatedAfter(date);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/number-greater-than/{number}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<User>> getUsersWithNumberGreaterThan(@PathVariable Long number) {
        List<User> users = userService.findUsersWithNumberGreaterThan(number);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search")
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<User>> getUsersByUsernamePattern(@RequestParam String pattern) {
        List<User> users = userService.findUsersByUsernamePattern(pattern);
        return ResponseEntity.ok(users);
    }

    @PostMapping
    @CrossOrigin(origins = "*")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @PutMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        try {

            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity
                .ok()
                .header("Access-Control-Allow-Origin", "http://localhost:3000")
                .header("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE")
                .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
                .header("Access-Control-Max-Age", "3600")
                .build();
    }
}