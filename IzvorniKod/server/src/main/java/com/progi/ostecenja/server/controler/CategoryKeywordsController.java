package com.progi.ostecenja.server.controler;

import com.progi.ostecenja.server.repo.CategoryKeywords;
import com.progi.ostecenja.server.service.CategoryKeywordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/keywords")
public class CategoryKeywordsController {
    @Autowired
    CategoryKeywordsService categoryKeywordsService;
    @GetMapping
    List<CategoryKeywords> getKeywords(){
       return categoryKeywordsService.listAll();
    }
}
