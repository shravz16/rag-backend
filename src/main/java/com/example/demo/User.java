package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "unique_number", nullable = false, unique = true)
    private Long uniqueNumber;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "email",nullable = false, unique = true)
    private String email;

    @Column(name = "project",nullable = false, unique = true)
    private String project;

    public String getFilecreatedat() {
        return filecreatedat;
    }

    public void setFilecreatedat(String filecreatedat) {
        this.filecreatedat = filecreatedat;
    }

    @Column(name = "filecreated")
    private String filecreatedat;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Long getUniqueNumber() { return uniqueNumber; }
    public void setUniqueNumber(Long uniqueNumber) { this.uniqueNumber = uniqueNumber; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProject() {

        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", uniqueNumber=" + uniqueNumber +
                ", createdAt=" + createdAt +
                '}';
    }
}