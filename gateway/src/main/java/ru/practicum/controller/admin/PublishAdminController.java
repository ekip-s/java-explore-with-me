package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.admin.PublishAdminClient;
import ru.practicum.dto.PublishDTO;
import ru.practicum.model.GetPublish;
import ru.practicum.model.Publish;


import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class PublishAdminController {

    private final PublishAdminClient publishClient;

    @GetMapping
    public ResponseEntity<Object> get(@RequestParam(required = false) List<Long> users,
                                      @RequestParam(required = false) List<String> states,
                                      @RequestParam(required = false) List<Long> categories,
                                      @RequestParam(required = false)
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime rangeStart,
                                      @RequestParam(required = false)
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime rangeEnd,
                                      @RequestParam(defaultValue = "0") Integer from,
                                      @RequestParam(defaultValue = "10") Integer size) {
        return publishClient.getPublishAdmin(new GetPublish(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Object> put(@Valid @RequestBody PublishDTO publish,
                                      @PathVariable Long eventId) {
        log.info("Приложение: admin_gateway. " +
                        "Получен PUT запрос к эндпоинту: '/admin/events'. ID = {}. Строка параметров запроса: {}",
                eventId ,publish.toString());
        return publishClient.putPublishAdmin(eventId, new Publish(publish));
    }

    @PatchMapping("/{eventId}/publish")
    public ResponseEntity<Object> approve(@PathVariable Long eventId) {
        log.info("Приложение: admin_gateway. Запрос на одобрение публикации с id = {}", eventId);
        return publishClient.approvePublishAdmin(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public ResponseEntity<Object> refuse(@PathVariable Long eventId) {
        log.info("Приложение: admin_gateway. Запрос на отмену публикации с id = {}", eventId);
        return publishClient.refusePublishAdmin(eventId);
    }
}
