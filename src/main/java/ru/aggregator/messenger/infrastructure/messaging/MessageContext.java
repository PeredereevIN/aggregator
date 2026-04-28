package ru.aggregator.messenger.infrastructure.messaging.chain;

import lombok.Data;
import ru.aggregator.messenger.infrastructure.persistence.Channel;
import ru.aggregator.messenger.infrastructure.persistence.Contact;
import ru.aggregator.messenger.infrastructure.persistence.Message;

/**
 * Контекст, передаваемый по цепочке обработчиков.
 */
@Data
public class MessageContext {
    private Channel channel;
    private Contact contact;
    private Message normalizedMessage;
    private boolean shouldBreak;  // флаг прерывания цепочки
}
