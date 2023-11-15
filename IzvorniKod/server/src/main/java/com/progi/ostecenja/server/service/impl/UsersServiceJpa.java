package com.progi.ostecenja.server.service.impl;

import com.progi.ostecenja.server.dao.UsersRepository;
import com.progi.ostecenja.server.repo.Users;
import com.progi.ostecenja.server.service.EntityMissingException;
import com.progi.ostecenja.server.service.RequestDeniedException;
import com.progi.ostecenja.server.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

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
    public Optional<Users> findByUserId(long userId) { return usersRepo.findById(userId); }


    @Override
    public Optional<Users> findByEmail(String email){
        Assert.notNull(email,"Email must be given");
        return usersRepo.findByEmail(email);
    }


    @Override
    public Users fetch(long userId) {
        return findByUserId(userId).orElseThrow(
                () -> new EntityMissingException(Users.class, userId)
        );
    }

    @Override
    public Users createUser(Users user) {
        validate(user);

        //Id validation
        Assert.isNull(user.getUserId(), "User ID must be null, not " + user.getUserId());

        //Unique Email
        if(usersRepo.countByEmail(user.getEmail()) > 0){
            throw new IllegalArgumentException("User with email '"+ user.getEmail() +"' already exists");
        }

        return usersRepo.save(user);
    }

    @Override
    public Users updateUser(Users user) {
        validate(user);
        Long userId = user.getUserId();

        //Id doesn't exist
        if (!usersRepo.existsById(userId))
            throw new EntityMissingException(Users.class, userId);

        //Email conflict
        if(usersRepo.existsByEmailAndUserIdNot(user.getEmail(),userId)){
            throw new RequestDeniedException("User with Email " + user.getEmail() + " already exists");
        }

        return usersRepo.save(user);
    }


    @Override
    public Users deleteUser(long id){
        Users user = fetch(id);
        usersRepo.delete(user);
        return user;
    }

    private void validate(Users user) {
        Assert.notNull(user, "User object must be given");

        //Email validation
        String email = user.getEmail();
        Assert.hasText(email, "Email can't be empty");
        Assert.isTrue(email.matches(EMAIL_REGEX), "Invalid email format");
        if(email.length()>100){
            Assert.isTrue(false,"Email can't be longer than 100 characters");
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
