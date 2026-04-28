package ru.aggregator.messenger.infrastructure.messaging.chain;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.aggregator.messenger.application.event.NewMessageEvent;

/**
 * Четвёртый обработчик: публикация события о новом сообщении.
 */
@Component
@Order(4)
@RequiredArgsConstructor
public class EventPublishingHandler implements MessageHandler {
    private final ApplicationEventPublisher publisher;

    @Override
    public boolean handle(MessageContext ctx) {
        publisher.publishEvent(new NewMessageEvent(this, ctx.getNormalizedMessage()));
        return true;
    }
}
