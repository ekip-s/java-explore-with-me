package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatsClient;
import ru.practicum.client.user.PublishUserClientPublic;
import ru.practicum.model.PublicGetPublish;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicPublishController {

    private final PublishUserClientPublic client;
    private final StatsClient statsClient;

    @GetMapping
    public ResponseEntity<Object> getEvents(@RequestParam(required = false) String text,
                                            @RequestParam(required = false) List<Long> categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false) @DateTimeFormat(
                                                    iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime rangeStart,
                                            @RequestParam(required = false) @DateTimeFormat(
                                                    iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime rangeEnd,
                                            @RequestParam(required = false) Boolean onlyAvailable,
                                            @RequestParam(required = false) String sort,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            HttpServletRequest httpServletRequest) {
        addStats(httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr());
        return client.getEventsPublic(
                new PublicGetPublish(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getEventsById(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        addStats(httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr());
        return client.getEventsByIdPublic(id);
    }

    private void addStats(String uri, String ip) {
        try {
            statsClient.addStatsNode(uri, ip);
        } catch (Throwable e) {
            System.out.println("Ошибка сервиса статистики.");
        }
    }
}
