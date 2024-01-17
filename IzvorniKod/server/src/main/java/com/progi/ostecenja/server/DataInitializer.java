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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private ImageService imageService;
    @EventListener
    public void appReady(ApplicationReadyEvent event){
        //initial users
        Users[] users = new Users[4];
        users[0] = new Users(null, "abc@gmail.com","xx","yy","pass1234");
        users[1] = new Users(null, "abc2@gmail.com","xx","yy","pass1234");
        users[2] = new Users(null, "abc3@gmail.com","xx","yy","pass1234");
        users[3] = new Users(null, "abc4@gmail.com","xx","yy","pass1234");
        for(Users user : users){
            if(!usersService.findByEmail(user.getEmail()).isPresent())
                usersService.createUser(user);
        }

        //initial cityOffices
        CityOffice[] offices = new CityOffice[4];
        offices[0] = new CityOffice(null, "Rupasti", "rupasti@gmail.com", "password");
        offices[1] = new CityOffice(null, "Rasvjeta", "rasvjeta@gmail.com", "password");
        offices[2] = new CityOffice(null, "Smeće", "smece@gmail.com", "password");
        offices[3] = new CityOffice(null, "Korupcija", "korupcija@gmail.com", "password");
        CityOffice[] offices2 = new CityOffice[4];
        for(int i=0; i<offices.length; i++){
            if(!cityOfficeService.findByCityOfficeEmail(offices[i].getCityOfficeEmail()).isPresent()){
                offices2[i] = cityOfficeService.createCityOffice(offices[i]);
            }else{
                offices2[i] = cityOfficeService.findByCityOfficeEmail(offices[i].getCityOfficeEmail()).get();
            }
        }

        //initial categories
        Category[] categories = new Category[6];
        categories[0] = new Category(null, "rupa na cesti", offices2[0].getCityOfficeId());
        categories[1] = new Category(null, "rupa na pješačkoj stazi", offices2[0].getCityOfficeId());
        categories[2] = new Category(null, "ulična rasvjeta", offices2[1].getCityOfficeId());
        categories[3] = new Category(null, "smeće na ulici", offices2[2].getCityOfficeId());
        categories[4] = new Category(null, "smeće u parku", offices2[2].getCityOfficeId());
        categories[5] = new Category(null, "institucionalna korupcija", offices2[3].getCityOfficeId());

        List<Category> categoryList = categoryService.listAll();
        Category[] categories2 = new Category[6];
        for(int i=0;i<categories.length;i++){
            boolean found=false;
            for(Category cat : categoryList){
                if(cat.getCategoryName().equals(categories[i].getCategoryName())){
                    categories2[i] = cat;
                    found=true;
                    break;
                }
            }
            if(!found)
                categories2[i] = categoryService.createCategory(categories[i]);
        }
        CategoryKeywords[] keywords = {
                new CategoryKeywords(null, "Rupa", categories2[0].getCategoryID()),
                new CategoryKeywords(null, "Cesta", categories2[0].getCategoryID()),
                new CategoryKeywords(null, "Pjesacki", categories2[1].getCategoryID()),
                new CategoryKeywords(null, "Pješački", categories2[1].getCategoryID()),
                new CategoryKeywords(null, "Rasvjeta", categories2[2].getCategoryID()),
                new CategoryKeywords(null, "Lampa", categories2[2].getCategoryID()),
                new CategoryKeywords(null, "Smeće", categories2[3].getCategoryID()),
                new CategoryKeywords(null, "Smrdi", categories2[3].getCategoryID()),
                new CategoryKeywords(null, "Park", categories2[4].getCategoryID()),
                new CategoryKeywords(null, "Otpad", categories2[4].getCategoryID()),
                new CategoryKeywords(null, "Mito", categories2[5].getCategoryID()),
                new CategoryKeywords(null, "Korupcija", categories2[5].getCategoryID()),
        };
        for(CategoryKeywords kw:keywords){
            boolean found=false;
            for(CategoryKeywords curKw : categoryKeywordsRepo.findAll()){
                if(curKw.getKeyword().equals(kw.getKeyword()) && curKw.getCategoryID()==kw.getCategoryID()){
                    found=true;
                    break;
                }
            }
            if(!found)
                categoryKeywordsRepo.save(kw);
        }


        List<Category> cats = categoryService.listAll();

        MultipartFile imageMulti;

        try(InputStream is = DataInitializer.class.getClassLoader().getResourceAsStream("mock.png")){
            assert is != null;
            imageMulti = new MockMultipartFile(
                    "data",
                    "mock.png",
                    "image/png",
                    is.readAllBytes()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<MultipartFile> images = new ArrayList<>();
        images.add(imageMulti);

        Report[] reports = {
                new Report(null, "Velika rupa na cesti",45.8000646, 15.978519, "Tu je neka velika rupa na raskrižju kod Lisinskog.", null, null, null,cats.get(0).getCategoryID())
        };
        for(Report report: reports){
            if(!reportService.getHeadlines().contains(report.getReportHeadline())){
                reportController.createReport(report.getReportID(), report.getReportHeadline(), report.getLat(), report.getLng(), report.getDescription(), report.getReportTS(), report.getUserID(),
                        report.getGroup().getReportID(),report.getCategoryID(), new StandardSession(null), images, "Lisinski");
            }
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
