package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.publish.AnswerPublishDTO;
import ru.practicum.model.publish.Publish;
import ru.practicum.model.publish.PublishDTO;
import ru.practicum.model.publish.PublishState;
import ru.practicum.service.PublishService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class PublishAdminController {

    private final PublishService publishService;

    @GetMapping
    public List<AnswerPublishDTO> get(@RequestParam(required = false) List<Long> users,
                             @RequestParam(required = false) List<PublishState> states,
                             @RequestParam(required = false) List<Long> categories,
                             @RequestParam(required = false)
                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                             @RequestParam(required = false)
                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                             @RequestParam(defaultValue = "0") Integer from,
                             @RequestParam(defaultValue = "10") Integer size) {
        return publishService.getPublishAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    public AnswerPublishDTO put(@RequestBody PublishDTO publish, @PathVariable Long eventId) {
        log.info("PUT запрос к эндпоинту: '/admin/events'. ID = {}." +
                " Строка параметров запроса: {}", eventId, publish.toString());
        return publishService.updatePublishAdmin(eventId, new Publish(publish));
    }

    @PatchMapping("/{eventId}/publish")
    public AnswerPublishDTO approve(@PathVariable Long eventId) {
        log.info("Запрос на одобрение публикации с id = {}", eventId);
        return publishService.approvePublishAdmin(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public AnswerPublishDTO refuse(@PathVariable Long eventId) {
        log.info("Запрос на отмену публикации с id = {}", eventId);
        return publishService.refusePublishAdmin(eventId);
    }
}
