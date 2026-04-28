package ru.aggregator.messenger.infrastructure.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.aggregator.messenger.domain.model.ChannelType;

@Data
public class ChannelConfigRequest {
    @NotNull
    private ChannelType type;
    @NotBlank
    private String configJson; // JSON-строка с токенами и настройками
}
