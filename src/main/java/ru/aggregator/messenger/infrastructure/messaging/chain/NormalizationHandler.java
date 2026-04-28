package ru.aggregator.messenger.infrastructure.messaging.chain;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Второй обработчик: нормализация (уже выполнена в оркестраторе, здесь может быть дополнительная логика).
 */
@Component
@Order(2)
public class NormalizationHandler implements MessageHandler {
    @Override
    public boolean handle(MessageContext ctx) {
        // Дополнительная валидация или обогащение, если нужно
        return true;
    }
}
