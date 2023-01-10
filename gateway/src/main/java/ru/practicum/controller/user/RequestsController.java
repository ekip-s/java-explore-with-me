package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.user.RequestsClient;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/users")
public class RequestsController {

    private final RequestsClient client;

    @GetMapping("/{userId}/requests")
    public ResponseEntity<Object> getRequests(@PathVariable Long userId) {
        return client.getRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    public ResponseEntity<Object> createRequests(@PathVariable Long userId, @RequestParam Long eventId) {
        return client.createRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<Object> cancelRequests(@PathVariable Long requestId, @PathVariable Long userId) {
        return client.cancelRequests(userId, requestId);
    }
}
