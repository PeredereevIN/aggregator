package ru.aggregator.messenger.infrastructure.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.aggregator.messenger.domain.model.ChannelType;
import ru.aggregator.messenger.domain.ports.MessagingConnector;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Фабрика коннекторов (паттерн Factory Method).
 * Собирает все реализации MessagingConnector из Spring-контекста
 * и предоставляет нужный по типу канала.
 */
@Component
@Slf4j
public class ConnectorFactory {
    private final Map<ChannelType, MessagingConnector> connectorMap;

    public ConnectorFactory(List<MessagingConnector> connectors) {
        this.connectorMap = connectors.stream()
                .collect(Collectors.toMap(MessagingConnector::getType, Function.identity()));
        log.info("Loaded connectors: {}", connectorMap.keySet());
    }

    public MessagingConnector getConnector(ChannelType type) {
        MessagingConnector connector = connectorMap.get(type);
        if (connector == null) {
            throw new IllegalArgumentException("No connector for type: " + type);
        }
        return connector;
    }
}
