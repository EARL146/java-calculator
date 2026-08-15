package com.example.hito.service;

import com.example.hito.dto.ContactRequest;
import com.example.hito.entity.ContactMessage;
import com.example.hito.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ContactService — handles saving contact form messages.
 */
@Service
public class ContactService {

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    /**
     * Convert the ContactRequest DTO → ContactMessage entity, then save to DB.
     */
    public ContactMessage saveMessage(ContactRequest request) {
        ContactMessage message = new ContactMessage();
        message.setName(request.getName());
        message.setContact(request.getContact());
        message.setMessage(request.getMessage());
        return contactMessageRepository.save(message);
    }
}
