package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.admin.UserAdminClient;
import ru.practicum.model.GetUsers;
import ru.practicum.model.User;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserAdminController {

    private final UserAdminClient userClient;


    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody User user) {
        log.info("Приложение: admin_gateway. " +
                "Получен POST запрос к эндпоинту: '/admin/users', Строка параметров запроса: {}", user.toString());
        return userClient.createUser(user);
    }

    @GetMapping
    public ResponseEntity<Object> get(@RequestParam(required = false) List<Long> ids,
                                          @RequestParam(defaultValue = "0") Integer from,
                                      @RequestParam(defaultValue = "10") Integer size) {
        return userClient.getUsers(new GetUsers(ids, checkPaginationParams(from, size), size));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable Long userId) {
        log.info("Приложение: admin_gateway. Получен DELETE запрос к эндпоинту: '/admin/users', userId: {}", userId);
        checkId(userId);
        return userClient.deleteUser(userId);
    }

    private void checkId(long id) {
        if (id < 1) {
            throw new IllegalArgumentException("Ошибка валидации: id не может быть меньше 1.");
        }
    }

    private Integer checkPaginationParams(int from, int size) {
        if (from < 0) {
            throw new IllegalArgumentException("Ошибка валидации: параметр from не может быть отрицательным.");
        } else if (size < 1) {
            throw new IllegalArgumentException("Ошибка валидации: параметр size должен быть больше нуля.");
        }
        return from / size;
    }
}
