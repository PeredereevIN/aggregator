package ru.aggregator.messenger.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aggregator.messenger.domain.model.ChannelType;
import ru.aggregator.messenger.domain.ports.MessagingConnector;
import ru.aggregator.messenger.infrastructure.messaging.ConnectorFactory;
import ru.aggregator.messenger.infrastructure.messaging.chain.MessageContext;
import ru.aggregator.messenger.infrastructure.messaging.chain.MessageProcessingChain;
import ru.aggregator.messenger.infrastructure.persistence.*;

/**
 * Оркестратор обработки входящего сообщения от вебхука.
 * Выполняет шаги:
 * 1. Получить коннектор по типу канала (Factory).
 * 2. Распарсить событие (Adapter).
 * 3. Найти или создать контакт (ContactService).
 * 4. Нормализовать в Message (Adapter).
 * 5. Передать в цепочку обработки (Chain of Responsibility).
 * Весь метод выполняется в одной транзакции.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class InboundMessageOrchestrator {

    private final ConnectorFactory connectorFactory;
    private final MessageProcessingChain processingChain;
    private final ChannelRepository channelRepository;
    private final ContactRepository contactRepository;

    @Transactional
    public void handleWebhook(ChannelType channelType, String payload, String signature) {
        MessagingConnector connector = connectorFactory.getConnector(channelType);

        connector.parseWebhook(payload, signature).ifPresent(event -> {
            // Получаем активный канал
            Channel channel = channelRepository.findByTypeAndEnabledTrue(channelType)
                    .orElseThrow(() -> new IllegalStateException("No enabled channel for " + channelType));

            // Находим или создаём контакт (автоматическое добавление при первом сообщении)
            Contact contact = contactRepository
                    .findByChannelIdAndExternalId(channel.getId(), event.userId())
                    .orElseGet(() -> {
                        Contact newContact = new Contact();
                        newContact.setChannel(channel);
                        newContact.setExternalId(event.userId());
                        // firstName/lastName можно получить позже вызовом API канала
                        newContact.setFirstName("Unknown");
                        return contactRepository.save(newContact);
                    });

            // Нормализация в сущность Message
            Message message = connector.normalize(event, channel, contact);

            // Создаём контекст и запускаем цепочку обработки
            MessageContext ctx = new MessageContext();
            ctx.setNormalizedMessage(message);
            ctx.setChannel(channel);
            ctx.setContact(contact);
            processingChain.process(ctx);

            if (ctx.isShouldBreak()) {
                log.info("Message was ignored by chain (e.g. duplicate): {}",
                        message.getIdempotencyKey());
            }
        });
    }
}
