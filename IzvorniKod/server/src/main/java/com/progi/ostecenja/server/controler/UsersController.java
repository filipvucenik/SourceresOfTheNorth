package com.progi.ostecenja.server.controler;

import com.progi.ostecenja.server.repo.Users;
import com.progi.ostecenja.server.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @GetMapping
    public List<Users> listUsers(){ return usersService.listAll(); }

    @PostMapping
    public Users createStudent(@RequestBody Users user) { return usersService.createUser(user); }
}
