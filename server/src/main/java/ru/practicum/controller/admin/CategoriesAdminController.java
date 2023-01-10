package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.Category;
import ru.practicum.service.CategoriesService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoriesAdminController {

    private final CategoriesService categoriesService;

    @PostMapping
    public Category create(@RequestBody Category category) {
        return categoriesService.createCategories(category);
    }

    @PatchMapping
    public Category patch(@RequestBody Category category) {
        return categoriesService.patchCategories(category);
    }

    @DeleteMapping("/{catId}")
    public void delete(@PathVariable Long catId) {
        categoriesService.deleteCategories(catId);
    }
}
