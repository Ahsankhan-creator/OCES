package com.project.OCES.Model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Notification {
    private String id;
    private String recipientId;
    private String message;
    private LocalDateTime timestamp;
    private boolean read;
    
    public Notification() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.read = false;
    }
    
    public Notification(String recipientId, String message) {
        this();
        this.recipientId = recipientId;
        this.message = message;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public String getRecipientId() { return recipientId; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public boolean isRead() { return read; }
    
    public void setRecipientId(String recipientId) { this.recipientId = recipientId; }
    public void setMessage(String message) { this.message = message; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setRead(boolean read) { this.read = read; }
}