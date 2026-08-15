package com.example.hito.controller;

import com.example.hito.dto.ContactRequest;
import com.example.hito.entity.ContactMessage;
import com.example.hito.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ContactController — handles the contact form submissions.
 *
 * POST /api/contact → saves the message to MySQL
 */
@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    /**
     * POST /api/contact
     * Receives the contact form data and saves it to the database.
     *
     * Frontend sends:
     * fetch("http://localhost:8080/api/contact", {
     *   method: "POST",
     *   headers: { "Content-Type": "application/json" },
     *   body: JSON.stringify({ name, contact, message })
     * })
     */
    @PostMapping
    public ResponseEntity<ContactMessage> submitContact(
            @Valid @RequestBody ContactRequest request) {

        ContactMessage saved = contactService.saveMessage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
