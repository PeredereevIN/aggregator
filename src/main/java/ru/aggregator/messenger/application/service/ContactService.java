package ru.aggregator.messenger.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aggregator.messenger.infrastructure.persistence.Contact;
import ru.aggregator.messenger.infrastructure.persistence.ContactRepository;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с контактами.
 */
@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public Contact getById(UUID id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found: " + id));
    }
}
