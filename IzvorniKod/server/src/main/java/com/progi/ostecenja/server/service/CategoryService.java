package com.progi.ostecenja.server.service;

import com.progi.ostecenja.server.repo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> listAll();

    Long getCityOfficeID(Long categoryID);

    Category createCategory(Category category);

    public Optional<Category> findByCategoryId(Long categoryID);
}
