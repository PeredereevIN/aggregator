package ru.aggregator.messenger.infrastructure.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.Instant;

@Data
@AllArgsConstructor
public class ApiError {
    private int status;
    private String message;
    private Instant timestamp;
}
