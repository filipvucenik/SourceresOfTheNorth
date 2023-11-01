package com.progi.ostecenja.server.service.impl;

import com.progi.ostecenja.server.dao.UsersRepository;
import com.progi.ostecenja.server.repo.Users;
import com.progi.ostecenja.server.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UsersServiceJpa implements UsersService {
    @Autowired
    private UsersRepository usersRepo;
    @Override
    public List<Users> listAll() {
        return usersRepo.findAll();
    }

    @Override
    public Users createUser(Users user) {
        return usersRepo.save(user);
    }
}
