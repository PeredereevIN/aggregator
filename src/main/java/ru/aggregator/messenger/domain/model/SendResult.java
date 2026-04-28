package ru.aggregator.messenger.domain.model;

/**
 * Результат отправки сообщения во внешний канал.
 * Используется в порту MessagingConnector.sendMessage().
 */
public record SendResult(
        boolean success,
        String externalMessageId, // ID, присвоенный внешней системой
        String error
) {
    public static SendResult ok(String externalMessageId) {
        return new SendResult(true, externalMessageId, null);
    }

    public static SendResult fail(String error) {
        return new SendResult(false, null, error);
    }
}
