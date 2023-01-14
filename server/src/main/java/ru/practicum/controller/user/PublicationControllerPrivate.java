package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.publish.AnswerPublicationDTO;
import ru.practicum.model.publish.Publication;
import ru.practicum.model.publish.PublicationDTO;
import ru.practicum.model.request.RequestDto;
import ru.practicum.service.PublicationService;
import ru.practicum.service.RequestsService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PublicationControllerPrivate {

    private final PublicationService publicationService;
    private final RequestsService requestsService;


    @GetMapping
    public List<AnswerPublicationDTO> getUserPublication(@PathVariable Long userId,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "10") Integer size) {
        return publicationService.getUserPublication(userId, from, size);
    }

    @PatchMapping
    public AnswerPublicationDTO updateUserPublication(@PathVariable Long userId, @RequestBody PublicationDTO publication) {
        return publicationService.updateUserPublication(userId, new Publication(publication), publication.getEventId());
    }

    @PostMapping
    public AnswerPublicationDTO addUserPublication(@PathVariable Long userId,
                                                   @RequestBody @Valid PublicationDTO publication) {
        log.info("Получен POST запрос к эндпоинту: '/users/{}/events'. Строка параметров запроса: {}", userId,
                publication.toString());
        return publicationService.addUserPublication(userId, new Publication(publication));
    }

    @GetMapping("/{eventId}")
    public AnswerPublicationDTO getUserPublicationById(@PathVariable Long eventId, @PathVariable Long userId) {
        return publicationService.getUserPublicationById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public AnswerPublicationDTO cancellationUserPublication(@PathVariable Long eventId, @PathVariable Long userId) {
        return publicationService.cancellationUserPublication(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> participationRequests(@PathVariable Long eventId, @PathVariable Long userId) {
        return requestsService.participationRequests(eventId, userId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public RequestDto approvalApplication(@PathVariable Long eventId, @PathVariable Long userId,
                                                      @PathVariable Long reqId) {
        return requestsService.approveApplicationPrivate(eventId, userId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public RequestDto cancellationApplication(@PathVariable Long eventId, @PathVariable Long userId,
                                                          @PathVariable Long reqId) {
        return requestsService.cancelApplicationPrivate(eventId, userId, reqId);
    }
}
