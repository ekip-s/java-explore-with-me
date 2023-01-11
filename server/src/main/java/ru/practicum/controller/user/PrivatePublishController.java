package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.publish.AnswerPublishDTO;
import ru.practicum.model.publish.Publish;
import ru.practicum.model.publish.PublishDTO;
import ru.practicum.model.request.RequestDto;
import ru.practicum.service.PublishService;
import ru.practicum.service.RequestsService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivatePublishController {

    private final PublishService publishService;
    private final RequestsService requestsService;


    @GetMapping
    public List<AnswerPublishDTO> getUserEvents(@PathVariable Long userId,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "10") Integer size) {
        return publishService.getUserEvents(userId, from, size);
    }

    @PatchMapping
    public AnswerPublishDTO updateUserEvents(@PathVariable Long userId, @RequestBody PublishDTO publish) {
        return publishService.updateUserEvents(userId, new Publish(publish), publish.getEventId());
    }

    @PostMapping
    public AnswerPublishDTO addUserEvents(@PathVariable Long userId, @RequestBody @Valid PublishDTO publish) {
        log.info("Получен POST запрос к эндпоинту: '/users/{}/events'. Строка параметров запроса: {}", userId,
                publish.toString());
        return publishService.addUserEvents(userId, new Publish(publish));
    }

    @GetMapping("/{eventId}")
    public AnswerPublishDTO getUserEventsById(@PathVariable Long eventId, @PathVariable Long userId) {
        return publishService.getUserEventsById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public AnswerPublishDTO cancellationUserEvents(@PathVariable Long eventId, @PathVariable Long userId) {
        return publishService.cancellationUserEvents(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> participationRequests(@PathVariable Long eventId, @PathVariable Long userId) {
        return requestsService.participationRequests(eventId, userId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public RequestDto approvalApplication(@PathVariable Long eventId, @PathVariable Long userId,
                                                      @PathVariable Long reqId) {
        return requestsService.approvalApplicationPrivate(eventId, userId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public RequestDto cancellationApplication(@PathVariable Long eventId, @PathVariable Long userId,
                                                          @PathVariable Long reqId) {
        return requestsService.cancellationApplicationPrivate(eventId, userId, reqId);
    }
}
