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
        offices[2] = new CityOffice(null, "Smece", "smece@gmail.com", "password");
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
        categories[1] = new Category(null, "rupa na pjesackome", offices2[0].getCityOfficeId());
        categories[2] = new Category(null, "ulicna rasvjeta", offices2[1].getCityOfficeId());
        categories[3] = new Category(null, "smece na ulici", offices2[2].getCityOfficeId());
        categories[4] = new Category(null, "smece u parku", offices2[2].getCityOfficeId());
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

        CategoryKeywords k1 = new CategoryKeywords(null, "Rupa", categories2[0].getCategoryID());
        CategoryKeywords k2 = new CategoryKeywords(null, "Smeće", categories2[3].getCategoryID());
        CategoryKeywords k3 = new CategoryKeywords(null, "Lampa", categories2[2].getCategoryID());
        CategoryKeywords k4 = new CategoryKeywords(null, "Korupcija", categories2[5].getCategoryID());
        categoryKeywordsRepo.save(k1);
        categoryKeywordsRepo.save(k2);
        categoryKeywordsRepo.save(k3);
        categoryKeywordsRepo.save(k4);


        List<Category> cats = categoryService.listAll();

        Report[] reports = {
                new Report(null, "OGROMANJSKA RUPA NA CESTI",45.8000646, 15.978519, "Tu je neka ogromanjska rupa kod lisinskog", null, null, null,cats.get(0).getCategoryID())
        };
        List<Report> reportList = new ArrayList<>();
        for(Report report: reports){
            if(!reportService.getHeadlines().contains(report.getReportHeadline())){
                reportList.add(reportController.createReport(report, new StandardSession(null)));
            }
        }

        Path relative = Path.of(System.getProperty("user.dir"));
        Path mockImagePath = relative.resolve("src/main/resources/mock.png");

        MultipartFile imageMulti;

        try(InputStream is = Files.newInputStream(mockImagePath)){
            imageMulti = new MockMultipartFile(
                    "data",
                    mockImagePath.getFileName().toString(),
                    "image/png",
                    is.readAllBytes()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String pathOnServer = reportController.uploadImage(imageMulti);
        boolean exists = false;
        for (Image image : imageService.listAllId(reports[0].getReportID()))
            if (image.getURL().equals(pathOnServer)) {
                exists = true;
                break;
            }

        if(!exists){
            List<Image> images = new ArrayList<>();
            images.add(new Image(null, reportList.get(0).getReportID(), pathOnServer));
            reportController.uploadedImages(images);
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
