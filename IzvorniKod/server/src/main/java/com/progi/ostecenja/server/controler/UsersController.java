package com.progi.ostecenja.server.controler;

import com.progi.ostecenja.server.repo.Users;
import com.progi.ostecenja.server.service.RequestDeniedException;
import com.progi.ostecenja.server.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @GetMapping
    public List<Users> listUsers(@RequestParam(defaultValue = "") String email, @RequestParam(defaultValue = "") String userName){
        if(email.equals("") && userName.equals("")){
            return usersService.listAll();
        }
        if(!email.equals("")&&!userName.equals("")){
            throw new RequestDeniedException("Email and Username both provided");
        }

        ArrayList<Users> list = new ArrayList<>();

        if(userName.equals("")){
            Optional<Users> Op = usersService.findByEmail(email);
            if(Op.isPresent()){
                Users user = Op.get();
                list.add(user);
                return list;
            }
        }else{
            Optional<Users> Op = usersService.findByUserName(userName);
            if(Op.isPresent()){
                Users user = Op.get();
                list.add(user);
                return list;
            }
        }
        return list;
    }

    @PostMapping
    public Users createStudent(@RequestBody Users user) { return usersService.createUser(user); }

    @GetMapping("/{id}")
    public Users getUser(@PathVariable("id") long id){ return usersService.fetch(id); }

    @PutMapping("/{id}")
    public Users updateUser(@PathVariable("id") long id, @RequestBody Users user){
        if(!user.getUserId().equals(id))
            throw new IllegalArgumentException("User ID must be preserved");
        return usersService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public Users deleteUser(@PathVariable("id") long id) { return usersService.deleteUser(id); }
}
