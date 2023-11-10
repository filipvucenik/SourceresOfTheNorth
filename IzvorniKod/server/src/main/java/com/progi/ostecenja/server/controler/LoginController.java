package com.progi.ostecenja.server.controler;

import jakarta.servlet.http.HttpSession;
import org.apache.juli.logging.Log;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {
    @GetMapping("/login")
    public String getLoginPage(HttpSession session){
        if(session.getAttribute("USER")!=null){
            Long userId = (Long) session.getAttribute("USER");
            String userIdString = userId.toString();
            return "redirect:/";
        }else{
            return "login.html";
        }
    }
}
