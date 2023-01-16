package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import java.net.URLDecoder;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.StatsNode;
import ru.practicum.model.StatsNodeDTO;
import ru.practicum.servise.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    public StatsNode add(@Valid @RequestBody StatsNode statsNode) {
        return statsService.addStat(statsNode);
    }

    @GetMapping("/stats")
    public List<StatsNodeDTO> getStats(@RequestParam String start,
                                       @RequestParam String end,
                                       @RequestParam(required = false) List<String> uris,
                                       @RequestParam(required = false, defaultValue = "true") Boolean unique
                                      ) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return statsService.getStat(LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8), format),
                LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8), format), uris, unique);
    }
}
