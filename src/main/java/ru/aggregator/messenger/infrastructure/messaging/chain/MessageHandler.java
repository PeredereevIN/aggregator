package ru.aggregator.messenger.infrastructure.messaging.chain;

/**
 * Интерфейс звена цепочки (Chain of Responsibility).
 */
public interface MessageHandler {
    /**
     * @return true — продолжить цепочку, false — прервать
     */
    boolean handle(MessageContext ctx);
}
