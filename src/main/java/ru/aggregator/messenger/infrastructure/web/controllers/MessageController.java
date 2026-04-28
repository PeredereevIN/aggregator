package ru.aggregator.messenger.infrastructure.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aggregator.messenger.application.service.OutboundMessageService;
import ru.aggregator.messenger.domain.model.MessageContent;
import ru.aggregator.messenger.domain.model.SendResult;
import ru.aggregator.messenger.infrastructure.web.dto.BulkSendRequest;
import ru.aggregator.messenger.infrastructure.web.dto.SendMessageRequest;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
@Tag(name = "Messages", description = "Отправка сообщений")
public class MessageController {

    private final OutboundMessageService outboundMessageService;

    @PostMapping("/send")
    @Operation(summary = "Отправить сообщение одному контакту")
    public ResponseEntity<SendResult> send(@Valid @RequestBody SendMessageRequest request) {
        MessageContent content = new MessageContent(request.getContent(), request.getContentType());
        SendResult result = outboundMessageService.sendToOne(request.getContactId(), content);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/send-bulk")
    @Operation(summary = "Массовая рассылка")
    public ResponseEntity<Void> sendBulk(@Valid @RequestBody BulkSendRequest request) {
        MessageContent content = new MessageContent(request.getContent(), request.getContentType());
        outboundMessageService.sendBulk(request.getContactIds(), content);
        return ResponseEntity.accepted().build();
    }
}
