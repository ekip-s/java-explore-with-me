package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.compilations.AnswerCompilationsDTO;
import ru.practicum.service.CompilationsService;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationsController {

    private final CompilationsService compilationsService;

    @GetMapping
    public List<AnswerCompilationsDTO> getCompilations(@RequestParam(defaultValue = "false") Boolean pinned,
                                                       @RequestParam(defaultValue = "0") Integer from,
                                                       @RequestParam(defaultValue = "10") Integer size) {
        return compilationsService.getCompilationsPublic(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public AnswerCompilationsDTO getCompilationsById(@PathVariable Long compId) {
        return compilationsService.getCompilationsByIdPublic(compId);
    }
}
