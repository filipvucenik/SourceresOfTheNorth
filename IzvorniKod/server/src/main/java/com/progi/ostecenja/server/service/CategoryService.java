package com.progi.ostecenja.server.service;

import com.progi.ostecenja.server.repo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryService {
    List<Category> listAll();
}
