package com.progi.ostecenja.server.controler;

import com.progi.ostecenja.server.repo.Report;
import com.progi.ostecenja.server.service.IStorageService;
import com.progi.ostecenja.server.service.ReportService;
import com.progi.ostecenja.server.service.impl.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    private  StorageService storageService;

    @Autowired
    private ReportService reportService;

    @GetMapping()
    public List<Report> listAllReports(){
        return reportService.listAll();
    }


    @PostMapping
    public Report createReport(@RequestBody Report report){

        return reportService.createReport(report);
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
    /*
    @GetMapping("/{id}")
    {

    }

    @PostMapping("/{id}")
    */
}
