package com.progi.ostecenja.server.controler;

import com.progi.ostecenja.server.repo.Category;
import com.progi.ostecenja.server.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private CategoryService categoryService;
    @GetMapping
    public List<Category> listCategories(){ return categoryService.listAll();}


}
