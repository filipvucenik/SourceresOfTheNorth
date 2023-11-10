package com.progi.ostecenja.server;

import com.progi.ostecenja.server.controler.UsersController;
import com.progi.ostecenja.server.repo.Users;
import com.progi.ostecenja.server.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {
    @Autowired
    private UsersService usersService;
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

    }
}
