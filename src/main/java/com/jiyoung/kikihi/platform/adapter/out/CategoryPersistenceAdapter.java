package com.jiyoung.kikihi.platform.adapter.out;

import com.jiyoung.kikihi.platform.adapter.out.jpa.category.CategoryJpaEntityRepository;
import com.jiyoung.kikihi.platform.application.port.out.category.CategoryPort;
import com.jiyoung.kikihi.platform.domain.category.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategoryPort {

    private final CategoryJpaEntityRepository repository;

    @Override
    public Category createCategory(Category category) {
        return null;
    }

    @Override
    public Optional<Category> loadCategoryById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Category> loadCategoryByCode(String code) {
        return Optional.empty();
    }

    @Override
    public boolean existsCategory(String code) {
        return false;
    }

    @Override
    public void deleteCategory(Long id) {

    }
}
