package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatsClient;
import ru.practicum.model.publish.AnswerPublishDTO;
import ru.practicum.model.SortOptions;
import ru.practicum.service.PublishService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicPublishController {

    private final PublishService publishService;
    private final StatsClient statsClient;

    @GetMapping
    public List<AnswerPublishDTO> getPublishPublic(@RequestParam(required = false) String text,
                                                   @RequestParam(required = false) List<Long> categories,
                                                   @RequestParam(required = false) Boolean paid,
                                                   @RequestParam(required = false)
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                   @RequestParam(required = false)
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                   @RequestParam(required = false) Boolean onlyAvailable,
                                                   @RequestParam(required = false) SortOptions sort,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "10") Integer size,
                                                   HttpServletRequest httpServletRequest) {
        addStats(httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr());
        return publishService.getPublishPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    public AnswerPublishDTO getEventsById(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        addStats(httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr());
        return publishService.getEventsById(id);
    }

    private void addStats(String uri, String ip) {
        try {
            statsClient.addStatsNode(uri, ip);
        } catch (Throwable e) {
            System.out.println("Ошибка сервиса статистики.");
        }
    }
}
