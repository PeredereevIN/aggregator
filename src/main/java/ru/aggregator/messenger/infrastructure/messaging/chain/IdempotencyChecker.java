package ru.aggregator.messenger.infrastructure.messaging.chain;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.aggregator.messenger.infrastructure.persistence.MessageRepository;

/**
 * Первый обработчик: проверка идемпотентности по idempotency_key.
 */
@Component
@Order(1)
@RequiredArgsConstructor
public class IdempotencyChecker implements MessageHandler {
    private final MessageRepository messageRepository;

    @Override
    public boolean handle(MessageContext ctx) {
        String key = ctx.getNormalizedMessage().getIdempotencyKey();
        if (key != null && messageRepository.existsByIdempotencyKey(key)) {
            return false; // дубликат, прерываем цепочку
        }
        return true;
    }
}
