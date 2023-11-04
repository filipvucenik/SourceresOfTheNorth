package com.progi.ostecenja.server.service.impl;

import com.progi.ostecenja.server.dao.UsersRepository;
import com.progi.ostecenja.server.repo.Users;
import com.progi.ostecenja.server.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
@Service
public class UsersServiceJpa implements UsersService {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    @Autowired
    private UsersRepository usersRepo;
    @Override
    public List<Users> listAll() {
        return usersRepo.findAll();
    }


    @Override
    public Users createUser(Users user) {
        validate(user);
        return usersRepo.save(user);
    }

    private void validate(Users user) {
        Assert.notNull(user, "User object must be given");

        //Id validation
        Assert.isNull(user.getUserId(), "User ID must be null, not " + user.getUserId());

        //Email validation
        String email = user.getEmail();
        Assert.hasText(email, "Email can't be empty");
        Assert.isTrue(email.matches(EMAIL_REGEX), "Invalid email format");
        if(usersRepo.countByEmail(email) > 0){
            throw new IllegalArgumentException("User with email '"+ user.getEmail() +"' already exists");
        }
        if(email.length()>100){
            Assert.isTrue(false,"Email can't be longer than 100 characters");
        }

        //Username validation
        String username = user.getUserName();
        Assert.hasText(username,"Username can't be empty");
        if(usersRepo.countByUserName(username) > 0){
            throw new IllegalArgumentException("User with username '"+ user.getUserName() +"' already exists");
        }
        if(username.length()>100){
            Assert.isTrue(false, "Username can't be longer than 100 characters");
        }

        //password validation
        String password = user.getPassword();
        Assert.hasText(password, "Password name can't be empty");
        if(password.length()<8){
            Assert.isTrue(false,"Password must have at least 8 characters");
        }else if(password.length()>100){
            Assert.isTrue(false, "Password can't be longer than 100 characters");
        }

        //firstName validation
        String firstName = user.getFirstName();
        Assert.hasText(firstName, "First name can't be empty");
        if(firstName.length()>50){
            Assert.isTrue(false,"First name can't be longer than 50 characters");
        }

        //lastName validation
        String lastName = user.getLastName();
        Assert.hasText(lastName, "Last name can't be empty");
        if(lastName.length()>50){
            Assert.isTrue(false,"Last name can't be longer than 50 characters");
        }
    }
}
