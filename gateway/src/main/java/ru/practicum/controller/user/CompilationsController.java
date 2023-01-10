package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.user.CompilationsClient;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationsController {

    private final CompilationsClient compilationsClient;

    @GetMapping
    public ResponseEntity<Object> getCompilations(@RequestParam(defaultValue = "false") Boolean pinned,
                                                  @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        return compilationsClient.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<Object> getCompilationsById(@PathVariable Long compId) {
        return compilationsClient.getCompilationsById(compId);
    }
}
