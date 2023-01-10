package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.user.PublishUserClientPrivate;
import ru.practicum.dto.PublishDTO;
import ru.practicum.model.Publish;

import javax.validation.Valid;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/users")
public class PrivatePublishController {

    private final PublishUserClientPrivate client;

    @GetMapping("/{userId}/events")
    public ResponseEntity<Object> getUserEvents(@PathVariable Long userId,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        return client.getUserEventsPrivate(userId, from, size);
    }

    @PatchMapping("/{userId}/events")
    public ResponseEntity<Object> updateUserEvents(@PathVariable Long userId, @RequestBody PublishDTO publish) {
        return client.updateUserEventsPrivate(userId, new Publish(publish), publish.getEventId());
    }

    @PostMapping("/{userId}/events")
    public ResponseEntity<Object> addUserEvents(@PathVariable Long userId, @Valid @RequestBody PublishDTO publish) {
        return client.addUserEventsPrivate(userId, new Publish(publish));
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<Object> getUserEventsById(@PathVariable Long eventId, @PathVariable Long userId) {
        return client.getUserEventsByIdPrivate(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<Object> cancellationUserEvents(@PathVariable Long eventId, @PathVariable Long userId) {
        return client.cancellationUserEventsPrivate(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<Object> participationRequests(@PathVariable Long eventId, @PathVariable Long userId) {
        return client.participationRequestsPrivate(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ResponseEntity<Object> approvalApplication(@PathVariable Long eventId, @PathVariable Long userId,
                                                      @PathVariable Long reqId) {
        return client.approvalApplicationPrivate(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ResponseEntity<Object> cancellationApplication(@PathVariable Long eventId, @PathVariable Long userId,
                                                          @PathVariable Long reqId) {
        return client.cancellationApplicationPrivate(userId, eventId, reqId);
    }
}
