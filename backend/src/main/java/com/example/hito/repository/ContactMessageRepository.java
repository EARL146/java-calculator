package com.example.hito.repository;

import com.example.hito.entity.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ContactMessageRepository — database access for ContactMessage entities.
 * All basic CRUD operations are inherited from JpaRepository.
 */
@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
}
