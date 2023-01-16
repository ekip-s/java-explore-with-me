package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.compilations.AnswerCompilationsDTO;
import ru.practicum.model.compilations.CompilationsDTO;
import ru.practicum.service.CompilationsService;

import javax.validation.Valid;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class CompilationsAdminController {

    private final CompilationsService compilationsService;

    @PostMapping
    public AnswerCompilationsDTO create(@RequestBody @Valid CompilationsDTO compilations) {
        log.info("Получен POST запрос к эндпоинту: '/admin/compilations'. Строка параметров запроса: {}",
                compilations.toString());
        return compilationsService.createCompilation(compilations);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilations(@PathVariable Long compId) {
        log.info("Получен DELETE запрос к эндпоинту: '/admin/compilations'. ID: {}", compId);
        compilationsService.deleteCompilations(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEvent(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Запрос: удаление события с id = {} в подборке с id = {}", eventId, compId);
        compilationsService.deleteEvent(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEvent(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Запрос: добавление события с id = {} в подборку с id = {}", eventId, compId);
       compilationsService.addEvent(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpin(@PathVariable Long compId) {
        log.info("Запрос: открепление подборки с id = {} с главной страницы.", compId);
        compilationsService.unpinAndFix(compId, false);
    }

    @PatchMapping("/{compId}/pin")
    public void toFix(@PathVariable Long compId) {
        log.info("Запрос: закрепление подборки с id = {} на главной странице.", compId);
        compilationsService.unpinAndFix(compId, true);
    }
}
