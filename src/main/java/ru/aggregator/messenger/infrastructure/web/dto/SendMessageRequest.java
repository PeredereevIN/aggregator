package ru.aggregator.messenger.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class SendMessageRequest {
    @NotNull
    private UUID contactId;
    @NotBlank
    private String content;
    private String contentType = "text";
}
