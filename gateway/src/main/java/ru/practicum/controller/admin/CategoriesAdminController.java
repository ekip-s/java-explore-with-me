package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.admin.CategoriesAdminClient;
import ru.practicum.model.Category;
import javax.validation.Valid;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoriesAdminController {

    private final CategoriesAdminClient categoriesClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody Category category) {
        log.info("Приложение: admin_gateway. " +
                        "Получен POST запрос к эндпоинту: '/admin/categories'. Строка параметров запроса: {}",
                category.toString());
        return categoriesClient.createCategories(category);
    }

    @PatchMapping
    public ResponseEntity<Object> patch(@Valid @RequestBody Category category) {
        log.info("Приложение: admin_gateway. " +
                        "Получен PATCH запрос к эндпоинту: '/admin/categories'. Строка параметров запроса: {}",
                category.toString());
        checkId(category.getId());
        return categoriesClient.patchCategories(category);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Object> delete(@PathVariable Long catId) {
        log.info("Приложение: admin_gateway. " +
                "Получен DELETE запрос к эндпоинту: '/admin/categories', с id: {}", catId);
        checkId(catId);
        return categoriesClient.deleteCategories(catId);
    }

    private void checkId(long id) {
        if (id < 1) {
            throw new IllegalArgumentException("Ошибка валидации: id не может быть меньше 1.");
        }
    }
}
