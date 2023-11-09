package com.progi.ostecenja.server.service.impl;

import com.progi.ostecenja.server.dao.CategoryKeywordsRepository;
import com.progi.ostecenja.server.repo.CategoryKeywords;
import com.progi.ostecenja.server.service.CategoryKeywordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryKeywordsServiceJpa implements CategoryKeywordsService {
    @Autowired
    CategoryKeywordsRepository categoryKeywordsRepo;
    @Override
    public List<CategoryKeywords> listAll(){return categoryKeywordsRepo.findAll();}

}
