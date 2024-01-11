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

    @GetMapping("/all")
    public List<Report> listReports(HttpSession session){
        Long userId = (Long) session.getAttribute("USER");
        Long officeID = (Long) session.getAttribute("OFFICE");
        if(userId != null){
            System.out.println("userid: " + userId);
            return reportService.listAllforUsers(userId);
        }else if (officeID != null){
            return reportService.listAllforOffice(officeID);
        }else {
            throw new IllegalStateException("Session error: both USER and OFFICE are null");
        }
    }

    @PostMapping("/group")
    public List<Long> groupReport(@RequestBody ReportDTO report, HttpSession session){
        ReportFilterDto filterDto = new ReportFilterDto();
        filterDto.setCategoryId(report.categoryID);
        filterDto.setStatus(null);
        filterDto.setRadius(0.2);
        filterDto.setLat(report.lat);
        filterDto.setLng(report.lng);
        filterDto.setStartDate(null);
        filterDto.setEndDate(null);

        List<Report> reportList = reportService.listAll();
        List<Report> returnList = new ArrayList<Report>();
        for(Report rp: reportList){
            if(rp.getCategoryID()==null || rp.getLat()==null || rp.getLng()==null) continue;
            if(rp.getCategoryID().equals(report.categoryID) && calculateDistance(rp.getLat(),rp.getLng(),report.lat,report.lng)<=0.2){
                double dist = calculateDistance(rp.getLat(),rp.getLng(),report.lat,report.lng);
                returnList.add(rp);
            }
        }
        List<Long> list = new ArrayList<Long>();
        if(reportList.isEmpty()){
            list.add((long)-1);
        }else{
            for(Report rp : returnList){
                list.add(rp.getReportID());
            }
        }
        return list;

    }
    @GetMapping("unhandled")
    public List<Report> listUnhandledReports(){
        return reportService.listAllUnhandled();
    }

    // TODO popraviti group ID
    @PostMapping
    public Report createReport(@RequestBody Report report, HttpSession session){
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
        Report saved = reportService.createReport(report);
        feedbackService.createFeedback(saved.getReportID(), timestamp);
        return saved;
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

    @PostMapping("/filtered")
    public List<Report> getReportsByFilter(@RequestBody ReportFilterDto reportFilterDto){
        return reportService.getReportsByFilter(reportFilterDto);
    }
    @GetMapping("/user/{userID}")
    public  List<Report> getReportsByUserId(@PathVariable Long userID){
        return reportService.getReportsByUserId(userID);
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Distance in kilometers
    }
}

class ReportDTO {
    String reportHeadline;
    double lat;
    double lng;
    long categoryID;

    public String getReportHeadline() {
        return reportHeadline;
    }

    public void setReportHeadline(String reportHeadline) {
        this.reportHeadline = reportHeadline;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(long categoryID) {
        this.categoryID = categoryID;
    }
}
