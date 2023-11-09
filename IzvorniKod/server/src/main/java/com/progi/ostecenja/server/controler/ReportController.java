package com.progi.ostecenja.server.controler;

import com.progi.ostecenja.server.repo.Image;
import com.progi.ostecenja.server.repo.Report;
import com.progi.ostecenja.server.service.CategoryService;
import com.progi.ostecenja.server.service.FeedbackService;
import com.progi.ostecenja.server.service.ReportGroupService;
import com.progi.ostecenja.server.service.ReportService;
import com.progi.ostecenja.server.service.impl.ImageService;
import com.progi.ostecenja.server.service.impl.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private CategoryService categoryService;

    @Autowired
    private ReportGroupService reportGroupService;

    @GetMapping()
    public List<Report> listAllReports(){
        return reportService.listAll();
    }


    @PostMapping
    public void createReport(@RequestBody Long reportID, String reportHeadline,
                             String location, String description, Long categoryID){
        Report report;
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        Long userID = 1L; // TREBA IZVLACITI IZ SESSIONA!!!
        Long groupID;
        if(false){
            //Neka logika za groupID
        }else{
            groupID = reportID;
            reportGroupService.createReportGroup(groupID);
        }
        report = new Report(reportID, reportHeadline, location, description,timestamp, userID, groupID, categoryID);

        reportService.createReport(report);
        feedbackService.createFeedback(groupID, categoryService.getCityOfficeID(categoryID), timestamp);
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
    /*
    @GetMapping()
    public Report getReport(@RequestParam("reportID") Long reportID)
    {
        return reportService.getReport(reportID);
    }

    @PutMapping()
    public Report changeStatus(@RequestParam("re"))
    */
}
