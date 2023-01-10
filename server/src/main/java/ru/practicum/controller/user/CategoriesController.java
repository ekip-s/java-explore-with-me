package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.Category;
import ru.practicum.service.CategoriesService;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoriesController {

    private final CategoriesService categoriesService;

    @GetMapping
    private Page<Category> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) {
        return categoriesService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    private Category getCategoriesById(@PathVariable Long catId) {
        return categoriesService.getCategoriesById(catId);
    }
}
