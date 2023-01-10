package ru.practicum.controller.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.Publish;
import ru.practicum.model.SortOptions;
import ru.practicum.service.PublishService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicPublishController {

    private final PublishService publishService;

    @PostMapping("/body")
    public Page<Publish> getEvents(@RequestBody PublicGetPublish publicGetPublish) {
        return publishService.getEvents(publicGetPublish.getText(), publicGetPublish.getCategories(),
                publicGetPublish.getPaid(), publicGetPublish.getRangeStart(), publicGetPublish.getRangeEnd(),
                publicGetPublish.getOnlyAvailable(), publicGetPublish.getSort(), publicGetPublish.getFrom(),
                publicGetPublish.getSize());
    }

    @GetMapping("/{id}")
    public Publish getEventsById(@PathVariable Long id) {
        return publishService.getEventsById(id);
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class PublicGetPublish {

    private String text;
    private List<Long> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private SortOptions sort;
    private int from;
    private int size;
}
