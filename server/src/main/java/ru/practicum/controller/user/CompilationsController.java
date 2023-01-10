package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.Compilations;
import ru.practicum.service.CompilationsService;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationsController {

    private final CompilationsService compilationsService;

    @GetMapping
    public Page<Compilations> getCompilations(@RequestParam(defaultValue = "false") Boolean pinned,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return compilationsService.getCompilationsPublic(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public Compilations getCompilationsById(@PathVariable Long compId) {
        return compilationsService.getCompilationsByIdPublic(compId);
    }
}
