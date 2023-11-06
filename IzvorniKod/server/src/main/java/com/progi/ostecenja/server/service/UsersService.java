package com.progi.ostecenja.server.service;

import com.progi.ostecenja.server.repo.Users;

import java.util.List;

public interface UsersService {
    /**
     * Lists all users in the system.
     * @return a list with all students
     */
    List<Users> listAll();

    /**
     * Creates new student in the system.
     * @param user object to create, with ID set to null
     * @return created student object in the system with ID set
     * @throws IllegalArgumentException if given student is null
     * or its ID is not null
     */
    Users createUser(Users user);
}
