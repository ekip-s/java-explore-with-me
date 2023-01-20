package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.reason.Reason;
import ru.practicum.service.ReasonService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/reason")
public class ReasonAdminController {

    private final ReasonService reasonService;

    @GetMapping("/{reasonId}")
    public Reason getReasonById(@PathVariable Long reasonId) {
        return reasonService.getReasonById(reasonId);
    }

    @GetMapping
    public List<Reason> getReason(@RequestParam(defaultValue = "0") Integer from,
                                  @RequestParam(defaultValue = "10") Integer size) {
        return reasonService.getReason(from, size);
    }

    @PostMapping
    public Reason addReason(@Valid @RequestBody Reason reason) {
        log.info("Получен POST запрос к эндпоинту: '/admin/reason'. Строка параметров запроса: {}",
                reason.toString());
        return reasonService.addReason(reason);
    }

    @PatchMapping("/{reasonId}")
    public Reason patchReason(@PathVariable Long reasonId, @Valid @RequestBody Reason reason) {
        log.info("Получен PATCH запрос к эндпоинту: '/admin/reason/{}'. Строка параметров запроса: {}", reasonId,
                reason.toString());
        return reasonService.patchReason(reasonId, reason);
    }

    @DeleteMapping("/{reasonId}")
    public void deleteReason(@PathVariable Long reasonId) {
        log.info("Получен DELETE запрос к эндпоинту: '/admin/reason/{}'.", reasonId);
        reasonService.deleteReason(reasonId);
    }
}
