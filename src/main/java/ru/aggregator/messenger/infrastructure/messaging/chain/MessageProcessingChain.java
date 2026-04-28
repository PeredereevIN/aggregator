package ru.aggregator.messenger.infrastructure.messaging.chain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Оркестратор цепочки обработки.
 */
@Component
@RequiredArgsConstructor
public class MessageProcessingChain {
    private final List<MessageHandler> handlers;

    public void process(MessageContext ctx) {
        for (MessageHandler handler : handlers) {
            if (!handler.handle(ctx)) {
                ctx.setShouldBreak(true);
                break;
            }
        }
    }
}
