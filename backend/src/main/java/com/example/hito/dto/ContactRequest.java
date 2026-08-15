package com.example.hito.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * ContactRequest DTO — shape of the contact form submission from the frontend.
 *
 * Frontend sends JSON like:
 * {
 *   "name": "Maria Santos",
 *   "contact": "09987654321",
 *   "message": "Do you deliver to Mandaue City?"
 * }
 */
public class ContactRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Contact (email or phone) is required")
    private String contact;

    @NotBlank(message = "Message is required")
    private String message;

    // ----------------------------------------------------------------
    // Getters and Setters
    // ----------------------------------------------------------------

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
