package ru.aggregator.messenger.infrastructure.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class BulkSendRequest {
    @NotEmpty
    private List<UUID> contactIds;
    @NotBlank
    private String content;
    private String contentType = "text";
}
