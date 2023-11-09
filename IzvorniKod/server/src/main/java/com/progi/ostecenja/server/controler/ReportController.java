package com.progi.ostecenja.server.controler;

import com.progi.ostecenja.server.repo.Image;
import com.progi.ostecenja.server.repo.Report;
import com.progi.ostecenja.server.service.ReportService;
import com.progi.ostecenja.server.service.impl.ImageService;
import com.progi.ostecenja.server.service.impl.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @GetMapping()
    public List<Report> listAllReports(){
        return reportService.listAll();
    }


    @PostMapping
    public Long createReport(@RequestBody Report report, Long OfficeId){

        Report newReport = reportService.createReport(report);

        return newReport.getReportID();
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
