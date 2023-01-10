package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.Compilations;
import ru.practicum.service.CompilationsService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class CompilationsAdminController {

    private final CompilationsService compilationsService;

    @PostMapping
    public Compilations create(@RequestBody Compilations compilations) {
        log.info("Приложение: server. " +
                        "Получен POST запрос к эндпоинту: '/admin/compilations'. Строка параметров запроса: {}",
                compilations.toString());
        return compilationsService.createCompilation(compilations);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilations(@PathVariable Long compId) {
        log.info("Приложение: server. Получен DELETE запрос к эндпоинту: '/admin/compilations'. ID: {}", compId);
        compilationsService.deleteCompilations(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public Compilations deleteEvent(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Приложение: server. Запрос: удаление события с id = {} в подборке с id = {}", eventId, compId);
        return compilationsService.deleteEvent(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public Compilations addEvent(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Приложение: server. " +
                "Запрос: добавление события с id = {} в подборку с id = {}", eventId, compId);
        return compilationsService.addEvent(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpin(@PathVariable Long compId) {
        log.info("Приложение: server. Запрос: открепление подборки с id = {} с главной страницы.", compId);
        compilationsService.unpinAndFix(compId, false);
    }

    @PatchMapping("/{compId}/pin")
    public void toFix(@PathVariable Long compId) {
        log.info("Приложение: server. Запрос: закрепление подборки с id = {} на главной странице.", compId);
        compilationsService.unpinAndFix(compId, true);
    }
}
