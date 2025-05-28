package com.project.OCES.Controller;

import com.project.OCES.Model.Notification;
import com.project.OCES.Service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
    @Autowired
    private DataService dataService;
    
    @GetMapping("/{recipientId}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable String recipientId) {
        return ResponseEntity.ok(dataService.getNotifications(recipientId));
    }
    
    @GetMapping("/unread-count/{recipientId}")
    public ResponseEntity<Integer> getUnreadCount(@PathVariable String recipientId) {
        return ResponseEntity.ok(dataService.getUnreadNotificationCount(recipientId));
    }
    
    @PostMapping("/mark-read/{recipientId}/{notificationId}")
    public ResponseEntity<Void> markAsRead(
            @PathVariable String recipientId,
            @PathVariable String notificationId) {
        dataService.markAsRead(recipientId, notificationId);
        return ResponseEntity.ok().build();
    }
}