package com.progi.ostecenja.server.controler;

import com.progi.ostecenja.server.repo.CityOffice;
import com.progi.ostecenja.server.repo.Users;
import com.progi.ostecenja.server.service.CityOfficeService;
import com.progi.ostecenja.server.service.UsersService;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UsersService usersService;
    private CityOfficeService officeService;

    @PostMapping("/userLogin")
    @ResponseBody
    public ResponseEntity<String> loginAuth(HttpSession session, @RequestBody UserCredentials userCredentials){

        String username= userCredentials.username;
        if(session.getAttribute("USER")!=null)
            return new ResponseEntity<>(session.getAttribute("USER").toString(), HttpStatus.OK);

        Users user = usersService.findByUserName(username).isPresent() ? usersService.findByUserName(username).get() : null;

        if(user==null)
            return new ResponseEntity<>("User doesn't exist", HttpStatus.BAD_REQUEST);

        session.setAttribute("USER",user.getUserId());
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }


    @PostMapping("/officeLogin")
    public ResponseEntity<String> officeAuth(HttpSession session,@RequestBody String email, @RequestBody String password){
        if(session.getAttribute("OFFICE")!=null)
            return new ResponseEntity<>(session.getAttribute("OFFICE").toString(), HttpStatus.OK);
        CityOffice office = officeService.findByCityOfficeEmail(email).isPresent() ? officeService.findByCityOfficeEmail(email).get() : null;


        if(office==null)
            return new ResponseEntity<>("User doesn't exist", HttpStatus.BAD_REQUEST);

        session.setAttribute("OFFICE",office.getCityOfficeId());
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
    
    @PostMapping("/userRegister")
    public ResponseEntity<String> userRegister(HttpSession session, @RequestBody Users user) {
        if (session.getAttribute("USER") != null)
            return new ResponseEntity<>(session.getAttribute("USER").toString(), HttpStatus.OK);

        Users createdUser = null;
        try {
            createdUser = usersService.createUser(user);
        } catch (Exception e) {

        }

        if (createdUser!=null){
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }
        return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
    }

}


class UserCredentials {
    public String username;
    public String password;

    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
}