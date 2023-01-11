package ru.practicum.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ForbiddenException extends RuntimeException {
    private final String reason;
    private final LocalDateTime timestamp;
    public ForbiddenException(String message, String reason) {
        super(message);
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
    }
}
