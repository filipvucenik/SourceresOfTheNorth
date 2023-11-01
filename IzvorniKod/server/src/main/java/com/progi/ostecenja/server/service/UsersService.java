package com.progi.ostecenja.server.service;

import com.progi.ostecenja.server.repo.Users;

import java.util.List;

public interface UsersService {
    List<Users> listAll();

    Users createUser(Users user);
}
