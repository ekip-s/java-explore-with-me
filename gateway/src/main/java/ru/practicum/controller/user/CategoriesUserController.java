package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.user.CategoriesClient;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoriesUserController {

    private final CategoriesClient categoriesClient;

    @GetMapping
    private ResponseEntity<Object> getCategoriesUser(@RequestParam(defaultValue = "0", required = false) Integer from,
                                                 @RequestParam(defaultValue = "10", required = false) Integer size) {
        return categoriesClient.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    private ResponseEntity<Object> getCategoriesByIdUser(@PathVariable Long catId) {
        return categoriesClient.getCategoriesById(catId);
    }
}
