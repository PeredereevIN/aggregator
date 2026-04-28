package ru.aggregator.messenger.infrastructure.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aggregator.messenger.application.service.ChannelConfigService;
import ru.aggregator.messenger.infrastructure.persistence.Channel;
import ru.aggregator.messenger.infrastructure.web.dto.ChannelConfigRequest;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/channels")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Управление каналами")
public class AdminController {

    private final ChannelConfigService channelConfigService;

    @GetMapping
    @Operation(summary = "Список всех каналов")
    public List<Channel> list() {
        return channelConfigService.getAllChannels();
    }

    @PostMapping
    @Operation(summary = "Добавить канал")
    public Channel add(@Valid @RequestBody ChannelConfigRequest request) {
        return channelConfigService.addChannel(request.getType(), request.getConfigJson());
    }

    @PutMapping("/{id}/disable")
    @Operation(summary = "Отключить канал")
    public ResponseEntity<Void> disable(@PathVariable UUID id) {
        channelConfigService.disableChannel(id);
        return ResponseEntity.ok().build();
    }
}
