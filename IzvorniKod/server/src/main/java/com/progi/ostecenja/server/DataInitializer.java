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
        Users[] users = new Users[4];
        users[0] = new Users(null, "abc@gmail.com","xx","yy","pass1234","username");
        users[1] = new Users(null, "abc2@gmail.com","xx","yy","pass1234","username2");
        users[2] = new Users(null, "abc3@gmail.com","xx","yy","pass1234","username3");
        users[3] = new Users(null, "abc4@gmail.com","xx","yy","pass1234","username4");
        for(Users user : users){
            usersService.createUser(user);
        }
        CityOffice cityOffice = new CityOffice(null, "Rupasti", "rupasti@gmail.com", "password");
        CityOffice cof = cityOfficeService.createCityOffice(cityOffice);
        Category rupe = new Category(null, "rupe", cof.getCityOfficeId());
        Category cat = categoryService.createCategory(rupe);
        Long cathegoryId = cat.getCategoryID() ;
        Report[] reports = new Report[2];
        reports[0] = new Report(null, "Rupa na cest", "Tu blizu", "Velika rupa na cesti", null, null, null, cathegoryId);
        reports[1] = new Report(null, "Druga rupa na cesti", "Tam malo dalje", "Neka rupa je negdi", null, null,null, cathegoryId);
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