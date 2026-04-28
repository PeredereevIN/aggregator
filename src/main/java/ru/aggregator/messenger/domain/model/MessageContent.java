package ru.aggregator.messenger.domain.model;

/**
 * Содержимое сообщения для отправки.
 * По умолчанию тип = "text". В будущем можно добавить image, document и т.д.
 */
public record MessageContent(
        String text,
        String type
) {
    public MessageContent(String text) {
        this(text, "text");
    }

    public static MessageContent text(String text) {
        return new MessageContent(text, "text");
    }
}
