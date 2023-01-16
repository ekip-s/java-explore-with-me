package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.publish.AnswerPublicationDTO;
import ru.practicum.model.publish.Publication;
import ru.practicum.model.publish.PublicationDTO;
import ru.practicum.model.publish.PublicationState;
import ru.practicum.service.PublicationService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class PublicationAdminController {

    private final PublicationService publicationService;

    @GetMapping
    public List<AnswerPublicationDTO> getPublication(@RequestParam(required = false) List<Long> users,
                                                     @RequestParam(required = false) List<PublicationState> states,
                                                     @RequestParam(required = false) List<Long> categories,
                                                     @RequestParam(required = false)
                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                     @RequestParam(required = false)
                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                     @RequestParam(defaultValue = "0") Integer from,
                                                     @RequestParam(defaultValue = "10") Integer size) {
        return publicationService.getPublicationAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    public AnswerPublicationDTO updatePublication(@RequestBody PublicationDTO publication, @PathVariable Long eventId) {
        log.info("PUT запрос к эндпоинту: '/admin/events'. ID = {}." +
                " Строка параметров запроса: {}", eventId, publication.toString());
        return publicationService.updatePublicationAdmin(eventId, new Publication(publication));
    }

    @PatchMapping("/{eventId}/publish")
    public AnswerPublicationDTO approvePublication(@PathVariable Long eventId) {
        log.info("Запрос на одобрение публикации с id = {}", eventId);
        return publicationService.approveOrRefusePublicationAdmin(eventId, PublicationState.PUBLISHED);
    }

    @PatchMapping("/{eventId}/reject")
    public AnswerPublicationDTO refusePublication(@PathVariable Long eventId) {
        log.info("Запрос на отмену публикации с id = {}", eventId);
        return publicationService.approveOrRefusePublicationAdmin(eventId, PublicationState.CANCELED);
    }
}
