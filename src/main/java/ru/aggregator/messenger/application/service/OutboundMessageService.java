package ru.aggregator.messenger.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aggregator.messenger.domain.model.MessageContent;
import ru.aggregator.messenger.domain.model.SendResult;
import ru.aggregator.messenger.domain.ports.MessagingConnector;
import ru.aggregator.messenger.infrastructure.messaging.ConnectorFactory;
import ru.aggregator.messenger.infrastructure.persistence.*;

/**
 * Сервис отправки исходящих сообщений (одиночных и массовых).
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OutboundMessageService {

    private final ConnectorFactory connectorFactory;
    private final ContactRepository contactRepository;
    private final MessageRepository messageRepository;

    @Transactional
    public SendResult sendToOne(UUID contactId, MessageContent content) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found: " + contactId));
        Channel channel = contact.getChannel();

        MessagingConnector connector = connectorFactory.getConnector(channel.getType());
        SendResult result = connector.sendMessage(contact, content);

        // Сохраняем копию исходящего сообщения в БД
        Message msg = Message.builder()
                .channel(channel)
                .contact(contact)
                .direction(Direction.OUT)
                .content(content.text())
                .contentType(content.type())
                .externalMessageId(result.externalMessageId())
                .timestamp(java.time.Instant.now())
                .processedAt(java.time.Instant.now())
                .build();
        messageRepository.save(msg);

        if (!result.success()) {
            log.warn("Failed to send message to contact {}: {}", contactId, result.error());
        }
        return result;
    }

    // Массовая рассылка
    public void sendBulk(java.util.List<UUID> contactIds, MessageContent content) {
        for (UUID id : contactIds) {
            try {
                sendToOne(id, content);
            } catch (Exception e) {
                log.error("Error sending to contact {}", id, e);
            }
        }
    }
}
