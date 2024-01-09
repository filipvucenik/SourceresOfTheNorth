package com.progi.ostecenja.server;

import com.progi.ostecenja.server.controler.ReportController;
import com.progi.ostecenja.server.controler.UsersController;
import com.progi.ostecenja.server.dao.CityOfficeRepository;
import com.progi.ostecenja.server.repo.*;
import com.progi.ostecenja.server.service.*;
import org.apache.catalina.session.StandardSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
/*
@Component
public class DataInitializer {
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
            if(cityOfficeService.findByCityOfficeEmail(offices[i].getCityOfficeEmail()).isPresent()){
                offices2[i] = cityOfficeService.createCityOffice(offices[i]);
            }
        }

        //initial categories
        Category[] categories = new Category[8];
        categories[0] = new Category(null, "rupa na cesti", offices2[0].getCityOfficeId());
        categories[1] = new Category(null, "rupa na pjesackome", offices2[0].getCityOfficeId());
        categories[2] = new Category(null, "ulicna rasvjeta", offices2[1].getCityOfficeId());
        categories[3] = new Category(null, "smece na ulici", offices2[2].getCityOfficeId());
        categories[4] = new Category(null, "smece u parku", offices2[2].getCityOfficeId());
        categories[5] = new Category(null, "institucionalna korupcija", offices2[3].getCityOfficeId());

        Category rupe = new Category(null, "rupe", cof.getCityOfficeId());
        Category cat = categoryService.createCategory(rupe);
        Long cathegoryId = cat.getCategoryID() ;



        Report[] reports = new Report[2];
        reports[0] = new Report(null, "Rupa na cest", 12.2, 12.3, "Velika rupa na cesti", null, null, null, cathegoryId);
        reports[1] = new Report(null, "Druga rupa na cesti", 12.2, 12.5, "Neka rupa je negdi", null, null,null, cathegoryId);
        for(Report report: reports){
            reportController.createReport(report, new StandardSession(null));
        }
    }
}
*/
/*
SELECT * FROM report;
SELECT * FROM city_office;
SELECT* FROM category;
SELECT * FROM REPORT_GROUP;
SELECT * FROM FEEDBACK;
 */