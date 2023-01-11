package ru.practicum.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CustomError {

    private HttpStatus status;
    private String reason;
    private String message;
    private LocalDateTime timestamp;
    private List<String> errors;
}
