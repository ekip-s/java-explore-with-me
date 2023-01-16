package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomError methodArgumentNotValidExceptionHandle(MethodArgumentNotValidException e) {
        return CustomError.builder()
                .errors(e.getFieldErrors().stream().map(Object::toString).collect(Collectors.toList()))
                .message(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                .reason(e.getParameter().getParameterName())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomError badRequestExceptionHandle(BadRequestException e) {
        return CustomError.builder()
                .errors(new ArrayList<>())
                .message(e.getMessage())
                .reason(e.getReason())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CustomError forbiddenExceptionHandle(ForbiddenException e) {
        return CustomError.builder()
                .status(HttpStatus.FORBIDDEN)
                .errors(new ArrayList<>())
                .message(e.getMessage())
                .reason(e.getReason())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public CustomError conflictExceptionHandle(ConflictException exc) {
        return CustomError.builder()
                .status(HttpStatus.CONFLICT)
                .message(exc.getMessage())
                .reason(exc.getReason())
                .timestamp(exc.getTimestamp())
                .build();
    }



    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public CustomError runtimeExceptionHandle(RuntimeException exc) {
        return CustomError.builder()
                .message(exc.getMessage())
                .reason("Error occurred")
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomError notFoundExceptionHandle(NotFoundException e) {
        return CustomError.builder()
                .message(e.getMessage())
                .reason(e.getReason())
                .status(HttpStatus.NOT_FOUND)
                .timestamp(e.getTimestamp())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomError missingServletRequestParameterExceptionHandle(MissingServletRequestParameterException exc) {
        return CustomError.builder()
                .message(exc.getMessage())
                .reason(String.format("Отсутствует параметр: ", exc.getParameterName()))
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
