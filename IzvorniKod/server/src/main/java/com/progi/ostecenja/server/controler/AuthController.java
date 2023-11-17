package com.progi.ostecenja.server.controler;

import com.progi.ostecenja.server.repo.CityOffice;
import com.progi.ostecenja.server.repo.Users;
import com.progi.ostecenja.server.service.CityOfficeService;
import com.progi.ostecenja.server.service.UsersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3030/", allowCredentials = "true")
public class AuthController {
    @Autowired
    private UsersService usersService;
    private CityOfficeService officeService;

    private BCryptPasswordEncoder pswdEncoder = new BCryptPasswordEncoder();

    @PostMapping("/userLogin")
    @ResponseBody
    public ResponseEntity<String> loginAuth(HttpSession session, @RequestBody UserCredentials userCredentials){

        String email= userCredentials.email;
        if(session.getAttribute("USER")!=null)
            return new ResponseEntity<>(session.getAttribute("USER").toString(), HttpStatus.OK);

        Users user = usersService.findByEmail(email).isPresent() ? usersService.findByEmail(email).get() : null;

        //userCredentials.password= pswdEncoder.encode(userCredentials.password);


        if(user==null)
            return new ResponseEntity<>("User doesn't exist", HttpStatus.BAD_REQUEST);
        if(pswdEncoder.matches(userCredentials.password, user.getPassword())){
              session.setAttribute("USER",user.getUserId());
              return new ResponseEntity<>("Success", HttpStatus.OK);   
        }
        return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);


    }


    @PostMapping("/officeLogin")
    public ResponseEntity<String> officeAuth(HttpSession session,@RequestBody String email, @RequestBody String password){
        if(session.getAttribute("OFFICE")!=null)
            return new ResponseEntity<>(session.getAttribute("OFFICE").toString(), HttpStatus.OK);
        CityOffice office = officeService.findByCityOfficeEmail(email).isPresent() ? officeService.findByCityOfficeEmail(email).get() : null;


        if(office==null)
            return new ResponseEntity<>("User doesn't exist", HttpStatus.BAD_REQUEST);
        if(office.getCityOfficePassword().equals(password)){
            session.setAttribute("OFFICE",office.getCityOfficeId());
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }
        return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
    }
    
    @PostMapping("/userRegister")
    public ResponseEntity<String> userRegister(HttpSession session, @RequestBody Users user) {
        if (session.getAttribute("USER") != null)
            return new ResponseEntity<>(session.getAttribute("USER").toString(), HttpStatus.OK);

        Users createdUser = null;

        try {
            createdUser = usersService.createUser(user);
        } catch (Exception e) {
            throw e;
        }
        if (createdUser!=null){
            session.setAttribute("USER",user.getUserId());   
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }
        return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
    }

}


class UserCredentials {
    public String email;
    public String password;

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }
}