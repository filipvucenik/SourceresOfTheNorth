package com.progi.ostecenja.server.controler;

import com.progi.ostecenja.server.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    EmailService emailService;

    @PostMapping("/test")
    public String sendMail(@RequestBody Map<String, String> jsonRequest) throws MessagingException {

        emailService.sendRequestSubmittedEmail(jsonRequest.get("to"), (long)1234);
        emailService.sendRequestStatusChange(jsonRequest.get("to"), (long)1234, "U obradi");
        return "Email sent!";
    }


}
