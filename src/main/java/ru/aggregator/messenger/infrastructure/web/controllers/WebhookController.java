package ru.aggregator.messenger.infrastructure.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aggregator.messenger.application.service.InboundMessageOrchestrator;
import ru.aggregator.messenger.domain.model.ChannelType;

@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
@Tag(name = "Webhooks", description = "Приём входящих сообщений")
public class WebhookController {

    private final InboundMessageOrchestrator orchestrator;

    @PostMapping("/vk")
    @Operation(summary = "Обработка вебхука VK")
    public ResponseEntity<String> vkCallback(
            @RequestBody String payload,
            @RequestHeader(value = "X-VK-Signature", required = false) String signature) {
        orchestrator.handleWebhook(ChannelType.VK, payload, signature);
        return ResponseEntity.ok("ok");
    }
}
