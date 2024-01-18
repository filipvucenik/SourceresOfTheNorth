package com.progi.ostecenja.server.controler;

import com.progi.ostecenja.server.repo.Report;
import com.progi.ostecenja.server.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    EmailService emailService;

    @PostMapping("/test")
    public String sendMail(@RequestBody Map<String, String> jsonRequest) throws MessagingException {

        //emailService.sendRequestSubmittedEmail(jsonRequest.get("to"), (long)1234);
        //emailService.sendRequestStatusChange(jsonRequest.get("to"), (long)1234, "U obradi");
        //emailService.sendRequestCategoryChange(jsonRequest.get("to"),(long)1234, "Oštećenje kolnika");
        //emailService.sendRequestDeleted(jsonRequest.get("to"), (long)1234);



        List<Report> groupedReports = new ArrayList<>();
        groupedReports.add(new Report((long)12345, "Test Report",45.8000646, 15.978519, "Test Report.", null, (long)352, null, (long)102));
        groupedReports.add(new Report((long)12346, "Test Report 2",45.8000646, 15.978519, "Test Report 2.", null, (long)352, null, (long)100));

        Report main =new Report((long)1234, "Main Report",45.8000646, 15.978519, "Main Report.", null, (long)352, null, (long)102);

        emailService.sendReportGroupedMain(main, groupedReports);
        return "Email sent!";
    }


}
