package ru.aggregator.messenger.infrastructure.messaging.chain;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.aggregator.messenger.infrastructure.persistence.MessageRepository;

/**
 * Третий обработчик: сохранение сообщения в БД.
 */
@Component
@Order(3)
@RequiredArgsConstructor
public class PersistenceHandler implements MessageHandler {
    private final MessageRepository messageRepository;

    @Override
    public boolean handle(MessageContext ctx) {
        messageRepository.save(ctx.getNormalizedMessage());
        return true;
    }
}
