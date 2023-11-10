package com.progi.ostecenja.server.service.impl;

import com.progi.ostecenja.server.dao.CategoryRepository;
import com.progi.ostecenja.server.repo.Category;
import com.progi.ostecenja.server.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceJpa implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepo;
   @Override
    public List<Category> listAll() {
        return categoryRepo.findAll();
    }
    @Override
    public Long getCityOfficeID(Long categoryID) {
        return categoryRepo.getReferenceById(categoryID).getCityOfficeID();
    }
}
