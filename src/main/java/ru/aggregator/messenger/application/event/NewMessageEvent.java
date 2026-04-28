package ru.aggregator.messenger.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.aggregator.messenger.infrastructure.persistence.Message;

/**
 * Событие "новое входящее сообщение обработано".
 * Используется для слабой связанности (Observer): слушатели могут подписаться
 * и выполнять свою логику (аналитика, уведомления), не изменяя ядро.
 */
@Getter
public class NewMessageEvent extends ApplicationEvent {
    private final Message message;

    public NewMessageEvent(Object source, Message message) {
        super(source);
        this.message = message;
    }
}
