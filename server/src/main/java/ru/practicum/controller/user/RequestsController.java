package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.request.RequestDto;
import ru.practicum.service.RequestsService;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/users")
public class RequestsController {

    private final RequestsService requestsService;

    @GetMapping("/{userId}/requests")
    public List<RequestDto> applicationsForOtherEvents(@PathVariable Long userId) {
        return requestsService.applicationsForOtherEvents(userId);
    }

    @PostMapping("/{userId}/requests")
    public RequestDto addRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        return requestsService.addRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Long requestId, @PathVariable Long userId) {
        return requestsService.cancelRequest(userId, requestId);
    }
}


