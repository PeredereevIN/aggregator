package ru.aggregator.messenger.infrastructure.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.aggregator.messenger.domain.model.*;
import ru.aggregator.messenger.domain.ports.MessagingConnector;
import ru.aggregator.messenger.infrastructure.persistence.Channel;
import ru.aggregator.messenger.infrastructure.persistence.Contact;
import ru.aggregator.messenger.infrastructure.persistence.Message;

import java.util.Optional;
import java.util.UUID;

/**
 * Реализация адаптера для ВКонтакте.
 * Паттерн Adapter: скрывает специфику VK API за интерфейсом MessagingConnector.
 */
@Component
@Slf4j
public class VkConnector implements MessagingConnector {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final Timer sendTimer;
    private final Timer parseTimer;

    public VkConnector(MeterRegistry meterRegistry) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.sendTimer = Timer.builder("connector.vk.send.time")
                .description("Time for sending VK message")
                .register(meterRegistry);
        this.parseTimer = Timer.builder("connector.vk.parse.time")
                .register(meterRegistry);
    }

    @Override
    public ChannelType getType() {
        return ChannelType.VK;
    }

    @Override
    public SendResult sendMessage(Contact recipient, MessageContent content) {
        return sendTimer.record(() -> {
            Channel channel = recipient.getChannel();
            JsonNode config = parseConfig(channel.getConfig());
            String token = config.get("access_token").asText();
            String apiUrl = "https://api.vk.com/method/messages.send";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String body = String.format(
                    "{\"user_id\":%s,\"message\":\"%s\",\"random_id\":%d,\"access_token\":\"%s\",\"v\":\"5.199\"}",
                    recipient.getExternalId(), content.text(), System.currentTimeMillis(), token
            );

            try {
                ResponseEntity<String> resp = restTemplate.postForEntity(
                        apiUrl, new HttpEntity<>(body, headers), String.class);
                // В реальном коде из ответа нужно извлечь message_id
                return SendResult.ok(UUID.randomUUID().toString());
            } catch (Exception e) {
                log.error("Failed to send VK message to {}", recipient.getExternalId(), e);
                return SendResult.fail(e.getMessage());
            }
        });
    }

    @Override
    public Optional<InboundEvent> parseWebhook(String payload, String signature) {
        return parseTimer.record(() -> {
            try {
                JsonNode root = objectMapper.readTree(payload);
                if (!root.has("type") || !"message_new".equals(root.get("type").asText())) {
                    return Optional.empty();
                }
                JsonNode msgNode = root.get("object").get("message");
                return Optional.of(new InboundEvent(
                        root.get("event_id").asText(),
                        msgNode.get("from_id").asText(),
                        msgNode.has("peer_id") ? msgNode.get("peer_id").asText() : msgNode.get("from_id").asText(),
                        msgNode.get("text").asText(),
                        msgNode.get("date").asText()
                ));
            } catch (Exception e) {
                log.error("Failed to parse VK webhook", e);
                return Optional.empty();
            }
        });
    }

    @Override
    public Message normalize(InboundEvent event, Channel channel, Contact contact) {
        return Message.builder()
                .channel(channel)
                .contact(contact)
                .conversationId(event.conversationId())
                .direction(Direction.IN)
                .content(event.text())
                .contentType("text")
                .externalMessageId(event.externalEventId())
                .idempotencyKey(channel.getId() + ":" + event.externalEventId())
                .timestamp(java.time.Instant.ofEpochSecond(Long.parseLong(event.timestamp())))
                .processedAt(java.time.Instant.now())
                .build();
    }

    private JsonNode parseConfig(String configJson) {
        try {
            return objectMapper.readTree(configJson);
        } catch (Exception e) {
            throw new RuntimeException("Invalid VK config", e);
        }
    }
}
