package com.progi.ostecenja.server;

import com.progi.ostecenja.server.controler.ReportController;
import com.progi.ostecenja.server.controler.UsersController;
import com.progi.ostecenja.server.dao.CategoryKeywordsRepository;
import com.progi.ostecenja.server.dao.CityOfficeRepository;
import com.progi.ostecenja.server.repo.*;
import com.progi.ostecenja.server.service.*;
import org.apache.catalina.session.StandardSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {
    @Autowired
    CategoryKeywordsRepository categoryKeywordsRepo;
    @Autowired
    private UsersService usersService;
    @Autowired
    private CityOfficeService cityOfficeService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private ReportController reportController;
    @EventListener
    public void appReady(ApplicationReadyEvent event){
        Users[] users = new Users[4];
        users[0] = new Users(null, "abc@gmail.com","xx","yy","pass1234");
        users[1] = new Users(null, "abc2@gmail.com","xx","yy","pass1234");
        users[2] = new Users(null, "abc3@gmail.com","xx","yy","pass1234");
        users[3] = new Users(null, "abc4@gmail.com","xx","yy","pass1234");
        for(Users user : users){
            usersService.createUser(user);
        }
        CityOffice cityOffice = new CityOffice(null, "Rupasti", "rupasti@gmail.com", "password");
        CityOffice smece = new CityOffice(null, "Ured za smeće", "smece@gmail.com", "password");
        CityOffice rasvjeta = new CityOffice(null,"Ured za uličnu rasvjetu", "struja@gmail.com", "password");

        CityOffice sm = cityOfficeService.createCityOffice(smece);
        CityOffice svj = cityOfficeService.createCityOffice(rasvjeta);
        CityOffice cof = cityOfficeService.createCityOffice(cityOffice);

        Category rupe = new Category(null, "rupe", cof.getCityOfficeId());
        Category smeceKat = new Category(null, "Smeće", sm.getCityOfficeId());
        Category rasvj = new Category(null, "Ulična rasvjeta", svj.getCityOfficeId());
        Category cat = categoryService.createCategory(rupe);
        Category smCat = categoryService.createCategory(smeceKat);
        Category rasvjCat = categoryService.createCategory(rasvj);

        CategoryKeywords k1 = new CategoryKeywords(null, "Rupa", rupe.getCategoryID());
        CategoryKeywords k2 = new CategoryKeywords(null, "Smeće", smeceKat.getCategoryID());
        CategoryKeywords k3 = new CategoryKeywords(null, "Lampa", rasvj.getCategoryID());
        categoryKeywordsRepo.save(k1);
        categoryKeywordsRepo.save(k2);
        categoryKeywordsRepo.save(k3);

        Long categoryId = cat.getCategoryID() ;
        Report[] reports = new Report[2];
        reports[0] = new Report(null, "Rupa na cest", "Tu blizu", "Velika rupa na cesti", null, null, null, categoryId);
        reports[1] = new Report(null, "Druga rupa na cesti", "Tam malo dalje", "Neka rupa je negdi", null, null,null, categoryId);
        for(Report report: reports){
            reportController.createReport(report, new StandardSession(null));
        }


    }
}
/*
SELECT * FROM report;
SELECT * FROM city_office;
SELECT* FROM category;
SELECT * FROM REPORT_GROUP;
SELECT * FROM FEEDBACK;
 */