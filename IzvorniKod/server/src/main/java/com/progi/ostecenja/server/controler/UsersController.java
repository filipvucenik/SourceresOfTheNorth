package com.progi.ostecenja.server.controler;

import com.progi.ostecenja.server.repo.Users;
import com.progi.ostecenja.server.service.RequestDeniedException;
import com.progi.ostecenja.server.service.UsersService;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3030/", allowCredentials = "true")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @GetMapping
    public List<Users> listUsers(@RequestParam(defaultValue = "") String email, @RequestParam(defaultValue = "") String userName){
        if(email.equals("")){
            return usersService.listAll();
        }

        ArrayList<Users> list = new ArrayList<>();


        Optional<Users> Op = usersService.findByEmail(email);
        if(Op.isPresent()){
            Users user = Op.get();
            list.add(user);
            return list;
        }
        return list;
    }

    @PostMapping
    public Users createUser(@RequestBody Users user) { return usersService.createUser(user); }

    @GetMapping("/{id}")
    public UserNoPass getUser(@PathVariable("id") long id){
        Users user = usersService.fetch(id);
        UserNoPass noPass = new UserNoPass(id, user.getEmail(), user.getFirstName(), user.getLastName());
        return noPass;
    }

    @PutMapping("/{id}")
    public Users updateUser(@PathVariable("id") long id, @RequestBody Users user){
        if(user.getUserId()==null)
            throw new IllegalArgumentException("User ID missing");
        if(!user.getUserId().equals(id))
            throw new IllegalArgumentException("User ID must be preserved");
        return usersService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public Users deleteUser(@PathVariable("id") long id) { return usersService.deleteUser(id); }
}

class UserNoPass {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;

    public UserNoPass(Long userId, String email, String firstName, String lastName) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}