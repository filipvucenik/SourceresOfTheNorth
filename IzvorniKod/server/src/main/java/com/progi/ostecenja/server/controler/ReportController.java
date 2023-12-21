package com.progi.ostecenja.server.controler;

import com.progi.ostecenja.server.dto.ReportFilterDto;
import com.progi.ostecenja.server.repo.Feedback;
import com.progi.ostecenja.server.repo.Image;
import com.progi.ostecenja.server.repo.Report;
import com.progi.ostecenja.server.service.CategoryService;
import com.progi.ostecenja.server.service.FeedbackService;
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

    @GetMapping("unhandled")
    public List<Report> listUnhandledReports(){
        return reportService.listAllUnhandled();
    }

    // TODO popraviti group ID
    @PostMapping
    public void createReport(@RequestBody Report report, HttpSession session){
        Timestamp timestamp = report.getReportTS();
        if(timestamp == null){
            timestamp = Timestamp.valueOf(LocalDateTime.now());
        }
        Long userId;
        try{
            userId  = (long)session.getAttribute("USER");

        }catch (RuntimeException e){
            userId = null;
        }

        report.setUserID(userId);
        report.setReportTS(timestamp);
        report.setGroup(null);
        reportService.createReport(report);


        feedbackService.createFeedback(report.getReportID(), timestamp);
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

    @GetMapping("/images/{id}")
    public List<Image> listImagesForRepordID(@PathVariable Long id){
        return imageService.listAllId(id);
    }
    @GetMapping("/{id}")
    public Report getReport(@PathVariable Long id)
    {
        return reportService.getReport(id);
    }

    @PostMapping("/updateStatus")
    public void changeStatus(@RequestParam Long reportID, String status){
        Long groupId = reportService.getReport(reportID).getGroup().getReportID();
        feedbackService.updateService(groupId, status);
    }

    @GetMapping("/filtered")
    public List<Report> getReportsByFilter(@RequestBody ReportFilterDto reportFilterDto){
        return reportService.getReportsByFilter(reportFilterDto);
    }
}
