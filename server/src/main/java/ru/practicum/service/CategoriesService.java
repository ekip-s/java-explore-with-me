package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoriesRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;

    @Autowired
    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    @Transactional
    public Category createCategories(Category category) {
        return categoriesRepository.save(category);
    }

    @Transactional
    public Category patchCategories(Category category) {
        getBuId(category.getId());
        return categoriesRepository.save(category);
    }

    @Transactional
    public void deleteCategories(long catId) {
        getBuId(catId);
        categoriesRepository.deleteById(catId);
    }

    public Page<Category> getCategories(int from, int size) {
        return categoriesRepository.findAll(PageRequest.of(from, size));
    }

    public Category getCategoriesById(long catId) {
        Optional<Category> category= categoriesRepository.findById(catId);
        if(category.isEmpty()) {
            return new Category();
        } else {
            return category.get();
        }
    }


    private Category getBuId(long id) {
        Optional<Category> optionalCategory = categoriesRepository.findById(id);

        if(optionalCategory.isEmpty()) {
            throw new NotFoundException("Ошибка: такой категории нет.");
        } else {
            return optionalCategory.get();
        }
    }
}
