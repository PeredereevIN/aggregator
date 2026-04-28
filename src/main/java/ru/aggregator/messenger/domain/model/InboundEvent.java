package ru.aggregator.messenger.domain.model;

/**
 * Каноническое представление входящего события из любого мессенджера.
 * Каждый коннектор преобразует свой формат (JSON VK, Telegram) в этот record.
 * Record — иммутабельный объект-значение.
 *
 * @param externalEventId  уникальный идентификатор события во внешней системе (для идемпотентности)
 * @param userId           ID пользователя во внешней системе
 * @param conversationId   ID беседы/чата (может совпадать с userId в личных сообщениях)
 * @param text             текст сообщения
 * @param timestamp        временная метка в строковом виде (как пришло)
 */
public record InboundEvent(
        String externalEventId,
        String userId,
        String conversationId,
        String text,
        String timestamp
) {}
