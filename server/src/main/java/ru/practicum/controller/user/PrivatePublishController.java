package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.Publish;
import ru.practicum.model.Request;
import ru.practicum.service.PublishService;
import ru.practicum.service.RequestsService;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/users")
public class PrivatePublishController {

    private final PublishService publishService;
    private final RequestsService requestsService;


    @GetMapping("/{userId}/events")
    public Page<Publish> getUserEvents(@PathVariable Long userId,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "10") Integer size) {
        return publishService.getUserEvents(userId, from, size);
    }

    @PatchMapping("/{userId}/events")
    public Publish updateUserEvents(@PathVariable Long userId, @RequestBody Publish publish,
                                    @RequestParam long eventId) {
        return publishService.updateUserEvents(userId, publish, eventId);
    }

    @PostMapping("/{userId}/events")
    public Publish addUserEvents(@PathVariable Long userId, @RequestBody Publish publish) {
        return publishService.addUserEvents(userId, publish);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public Publish getUserEventsById(@PathVariable Long eventId, @PathVariable Long userId) {
        return publishService.getUserEventsById(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public Publish cancellationUserEvents(@PathVariable Long eventId, @PathVariable Long userId) {
        return publishService.cancellationUserEvents(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public Request participationRequests(@PathVariable Long eventId, @PathVariable Long userId) {
        return requestsService.participationRequests(eventId, userId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public void approvalApplication(@PathVariable Long eventId, @PathVariable Long userId,
                                                      @PathVariable Long reqId) {
        requestsService.approvalApplicationPrivate(eventId, userId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public void cancellationApplication(@PathVariable Long eventId, @PathVariable Long userId,
                                                          @PathVariable Long reqId) {
        requestsService.cancellationApplicationPrivate(eventId, userId, reqId);
    }
}
