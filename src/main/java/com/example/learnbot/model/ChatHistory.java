package com.example.learnbot.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_history")
public class ChatHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Use @Column(columnDefinition = "TEXT") to store long AI answers or questions
    @Column(columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String response;

    // This links the history to a specific User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime createdAt = LocalDateTime.now();

    // --- CONSTRUCTORS ---
    public ChatHistory() {}

    // --- GETTERS AND SETTERS (Fixed Names) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQuestion() { return question; }
    // This is the method your BotController was missing!
    public void setQuestion(String question) { this.question = question; }

    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}