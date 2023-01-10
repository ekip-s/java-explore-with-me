package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.admin.CompilationsAdminClient;
import ru.practicum.dto.CompilationsDTO;
import ru.practicum.model.Compilations;



import javax.validation.Valid;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class CompilationsAdminController {

    private final CompilationsAdminClient compilationsClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody CompilationsDTO compilations) {
        log.info("Приложение: admin_gateway. " +
                        "Получен POST запрос к эндпоинту: '/admin/compilations'. Строка параметров запроса: {}",
                compilations.toString());
        return compilationsClient.createCompilations(new Compilations(compilations));
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Object> deleteCompilations(@PathVariable Long compId) {
        log.info("Приложение: admin_gateway. Получен DELETE запрос к эндпоинту: '/admin/compilations'. ID: {}", compId);
        return compilationsClient.deleteCompilations(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public ResponseEntity<Object> deleteEvent(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Приложение: admin_gateway. Запрос: удаление события с id = {} в подборке с id = {}", eventId, compId);
        return compilationsClient.deleteEvent(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public ResponseEntity<Object> addEvent(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Приложение: admin_gateway. " +
                "Запрос: добавление события с id = {} в подборку с id = {}", eventId, compId);
        return compilationsClient.addEvent(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public ResponseEntity<Object> unpin(@PathVariable Long compId) {
        log.info("Приложение: admin_gateway. Запрос: открепление подборки с id = {} с главной страницы.", compId);
        return compilationsClient.unpinCompilations(compId);
    }

    @PatchMapping("/{compId}/pin")
    public ResponseEntity<Object> toFix(@PathVariable Long compId) {
        log.info("Приложение: admin_gateway. Запрос: закрепление подборки с id = {} на главной странице.", compId);
        return compilationsClient.toFixCompilations(compId);
    }
}
