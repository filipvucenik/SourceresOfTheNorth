package com.progi.ostecenja.server.controler;

import com.progi.ostecenja.server.repo.Feedback;
import com.progi.ostecenja.server.repo.Image;
import com.progi.ostecenja.server.repo.Report;
import com.progi.ostecenja.server.service.CategoryService;
import com.progi.ostecenja.server.service.FeedbackService;
import com.progi.ostecenja.server.service.ReportGroupService;
import com.progi.ostecenja.server.service.ReportService;
import com.progi.ostecenja.server.service.ImageService;
import com.progi.ostecenja.server.service.impl.StorageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    private  StorageService storageService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private ReportGroupService reportGroupService;
    @GetMapping("/all")
    public List<Report> listReports(HttpSession session){
        Long userId = (Long) session.getAttribute("USER");
        Long officeID = (Long) session.getAttribute("OFFICE");
        if(userId != null){
            return reportService.listAllforUsers(userId);
        }else if (officeID != null){
            return reportService.listAllforOffice(officeID);
        }else {
            throw new IllegalStateException("Session error: both USER and OFFICE are null");
        }

    }


    @PostMapping
    public void createReport(@RequestBody Report report, HttpSession session){
        Timestamp timestamp = report.getReportTS();
        if(timestamp == null){
            timestamp = Timestamp.valueOf(LocalDateTime.now());
        }
        Long groupID = report.getGroupID();
        if(report.getGroupID() == null){
            groupID = report.getReportID();
            report.setGroupID(groupID);
            reportGroupService.createReportGroup(groupID);
        }
        report.setUserID((Long) session.getAttribute("USER"));
        reportService.createReport(report);
        feedbackService.createFeedback(groupID, timestamp);
    }

    @PostMapping("/uploadImage")
    public String uploadImage(@RequestParam("imageFile") MultipartFile imageFile){
        String returnValue = "";
        try{
            returnValue = storageService.saveImage(imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return returnValue;
    }

    @PostMapping("/uploadImagesPath")
    public List<Image> uploadedImages(@RequestParam("images") List<Image> images){
        return imageService.fillImages(images);
    }

    @GetMapping("/images")
    public List<Image> listImagesForRepordID(@RequestParam("reportID") Long reportID){
        return imageService.listAllId(reportID);
    }
    @GetMapping("/id")
    public Report getReport(@RequestParam("reportID") Long reportID)
    {
        return reportService.getReport(reportID);
    }

    @PostMapping("/updateStatus")
    public void changeStatus(@RequestParam Long reportID, String status){
        Long groupId = reportService.getReport(reportID).getGroupID();
        feedbackService.updateService(groupId, status);
    }
}
