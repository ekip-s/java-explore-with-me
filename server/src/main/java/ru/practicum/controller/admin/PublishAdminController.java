package ru.practicum.controller.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.Publish;
import ru.practicum.model.PublishState;
import ru.practicum.service.PublishService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class PublishAdminController {

    private final PublishService publishService;

    @PostMapping("/body")
    public Page<Publish> get(@RequestBody GetPublish getPublish) {
        return publishService.getPublishAdmin(getPublish.getUsers(), getPublish.getStates(), getPublish.getCategories(),
                getPublish.getRangeStart(), getPublish.getRangeEnd(), getPublish.getFrom(), getPublish.getSize());
    }

    @PutMapping("/{eventId}")
    public Publish put(@RequestBody Publish publish,
                                      @PathVariable Long eventId) {
        log.info("Приложение: server. " +
                        "Получен PUT запрос к эндпоинту: '/admin/events'. ID = {}. Строка параметров запроса: {}",
                eventId ,publish.toString());
        return publishService.updatePublishAdmin(eventId, publish);
    }

    @PatchMapping("/{eventId}/publish")
    public Publish approve(@PathVariable Long eventId) {
        log.info("Приложение: server. Запрос на одобрение публикации с id = {}", eventId);
        return publishService.approvePublishAdmin(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public Publish refuse(@PathVariable Long eventId) {
        log.info("Приложение: server. Запрос на отмену публикации с id = {}", eventId);
        return publishService.refusePublishAdmin(eventId);
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class GetPublish {

    private List<Long> users;
    private List<PublishState> states;
    private List<Long> categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private int from;
    private int size;
}
