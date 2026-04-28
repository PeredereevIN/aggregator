package ru.aggregator.messenger.application.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Пример слушателя события NewMessageEvent.
 * Здесь может быть сохранение в аналитику, отправка уведомления и т.д.
 */
@Component
@Slf4j
public class MessageEventListener {

    @EventListener
    public void onNewMessage(NewMessageEvent event) {
        log.info("New message from contact {}: {}",
                event.getMessage().getContact().getExternalId(),
                event.getMessage().getContent());
        // Здесь можно добавить сохранение в отдельную таблицу аналитики
    }
}
