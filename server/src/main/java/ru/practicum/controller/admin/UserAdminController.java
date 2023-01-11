package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.user.User;
import ru.practicum.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserAdminController {

    private final UserService userService;

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        log.info("Получен POST запрос к эндпоинту: '/admin/users', Строка параметров запроса: {}", user.toString());
        return userService.createUser(user);
    }

    @GetMapping
    public List<User> get(@RequestParam(required = false) List<Long> ids,
                          @RequestParam(defaultValue = "0") Integer from,
                          @RequestParam(defaultValue = "10") Integer size) {
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("Приложение: admin_gateway. Получен DELETE запрос к эндпоинту: '/admin/users', userId: {}", userId);
        userService.deleteUser(userId);
    }
}