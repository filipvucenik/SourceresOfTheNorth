package com.progi.ostecenja.server.service.impl;

import com.progi.ostecenja.server.dao.CategoryKeywordsRepository;
import com.progi.ostecenja.server.repo.categoryKeywords;
import com.progi.ostecenja.server.service.CategoryKeywordsService;
import com.progi.ostecenja.server.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryKeywordsServiceJpa implements CategoryKeywordsService {
    @Autowired
    CategoryKeywordsRepository categoryKeywordsRepo;
    @Override
    public List<categoryKeywords> listAll(){return categoryKeywordsRepo.findAll();}

}
