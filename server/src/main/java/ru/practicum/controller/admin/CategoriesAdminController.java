package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.category.Category;
import ru.practicum.service.CategoriesService;
import javax.validation.Valid;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoriesAdminController {

    private final CategoriesService categoriesService;

    @PostMapping
    public Category createCategories(@Valid @RequestBody Category category) {
        log.info("Получен POST запрос к эндпоинту: '/admin/categories'. Строка параметров запроса: {}",
                category.toString());
        return categoriesService.createCategories(category);
    }

    @PatchMapping
    public Category changeCategories(@Valid @RequestBody Category category) {
        log.info("Получен PATCH запрос к эндпоинту: '/admin/categories'. Строка параметров запроса: {}",
                category.toString());
        return categoriesService.patchCategories(category);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategories(@PathVariable Long catId) {
        log.info("Получен DELETE запрос к эндпоинту: '/admin/categories', с id: {}", catId);
        categoriesService.deleteCategories(catId);
    }
}
