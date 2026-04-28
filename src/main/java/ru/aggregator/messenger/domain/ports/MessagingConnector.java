package ru.aggregator.messenger.domain.ports;

import ru.aggregator.messenger.domain.model.*;
import java.util.Optional;

/**
 * Порт-адаптер (интерфейс) для всех мессенджеров.
 * Паттерн Adapter: каждый канал (VK, Telegram) реализует этот интерфейс,
 * скрывая свою специфику за общей сигнатурой.
 * Паттерн Strategy: каждая реализация — это стратегия взаимодействия с конкретным API.
 */
public interface MessagingConnector {

    /**
     * @return тип канала, который обслуживает этот коннектор
     */
    ChannelType getType();

    /**
     * Отправка сообщения пользователю.
     * Реализация инкапсулирует вызов внешнего API.
     */
    SendResult sendMessage(Contact recipient, MessageContent content);

    /**
     * Парсинг вебхука от мессенджера в каноническое представление.
     * Возвращает Optional.empty(), если событие не является входящим сообщением.
     */
    Optional<InboundEvent> parseWebhook(String payload, String signature);

    /**
     * Нормализация события в сущность Message.
     * Коннектор сам отвечает за преобразование своих данных во внутренний формат.
     */
    Message normalize(InboundEvent event, Channel channel, Contact contact);
}
