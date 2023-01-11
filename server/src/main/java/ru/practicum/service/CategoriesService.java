package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.category.Category;
import ru.practicum.repository.CategoriesRepository;
import ru.practicum.validation.ValidationMaster;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CategoriesService extends ValidationMaster {

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
        checkId(catId);
        getBuId(catId);
        categoriesRepository.deleteById(catId);
    }

    public List<Category> getCategories(int from, int size) {
        return categoriesRepository.findAll(checkPaginationParams(from, size))
                .stream()
                .collect(Collectors.toList());
    }

    public Category getCategoriesById(long catId) {
        checkId(catId);
        return getBuId(catId);
    }


    private Category getBuId(long id) {
        Optional<Category> optionalCategory = categoriesRepository.findById(id);

        if(optionalCategory.isEmpty()) {
            throw new NotFoundException("Некорректный запрос.", "Такой категории нет.");
        } else {
            return optionalCategory.get();
        }
    }
}
