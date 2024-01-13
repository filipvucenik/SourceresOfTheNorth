package com.progi.ostecenja.server.controler;

import com.progi.ostecenja.server.dto.ReportByStatusDTO;
import com.progi.ostecenja.server.dto.ReportFilterDto;
import com.progi.ostecenja.server.dto.StatisticDTO;
import com.progi.ostecenja.server.repo.*;
import com.progi.ostecenja.server.service.*;
import com.progi.ostecenja.server.service.impl.StorageService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    private UsersService usersService;
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
    private EmailService emailService;

    @GetMapping("/all")
    public List<ReportImage> listReports(HttpSession session){
        Long userId = (Long) session.getAttribute("USER");
        Long officeID = (Long) session.getAttribute("OFFICE");
        List<Report> reports;
        List<ReportImage> ret = new ArrayList<>();
        if(userId != null){
            System.out.println("userid: " + userId);
            reports =  reportService.listAllforUsers(userId);

        }else if (officeID != null){
            reports =  reportService.listAllforOffice(officeID);
        }else {
            throw new IllegalStateException("Session error: both USER and OFFICE are null");
        }
        for(Report rep: reports){
            List<Image> images = imageService.listAllId(rep.getReportID());
            ret.add(new ReportImage(rep, images));
        }
        return ret;
    }

    @PostMapping("/group")
    public List<ReportCategory> groupReport(@RequestBody ReportDTO report, HttpSession session){
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
        Map<Long, Category> categories =categoryService.listAll().stream().collect(Collectors.toMap(Category::getCategoryID, c -> c));
        List<ReportCategory> ret = new ArrayList<>();
        for (Report rep: returnList){
            ret.add(new ReportCategory(rep, categories.get(rep.getCategoryID())));
        }
    return ret;

    }
    @GetMapping("unhandled")
    public List<ReportImage> listUnhandledReports(){
        List<Report> reports = reportService.listAllUnhandled();
        List<ReportImage> ret = new ArrayList<>();
        for(Report rep: reports){
            List<Image> images = imageService.listAllId(rep.getReportID());
            ret.add(new ReportImage(rep, images));
        }
        return ret;
    }

    @PostMapping
    public Report createReport(@RequestBody Report report, HttpSession session, List<MultipartFile> images){
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

        Users user = usersService.fetch(userId);
        try {
            emailService.sendRequestSubmittedEmail(user.getEmail(), saved.getReportID());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        List<Image> imagePaths = new ArrayList<>();
        if(images != null){
            for(MultipartFile image: images){
                String path;
                try {
                    path = storageService.saveImage(image);
                } catch (IOException e){
                    throw new RuntimeException(e.getMessage());
                }
                imagePaths.add(new Image(null, saved.getReportID(), path));
            }

            imageService.fillImages(imagePaths);
        }


        return saved;
    }
    /*
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

     */

    @GetMapping("/{id}")
    public ReportImage getReport(@PathVariable Long id)
    {
        Report report = reportService.getReport(id);
        return new ReportImage(report, imageService.listAllId(report.getReportID()));
    }

    @PutMapping ("/updateStatus")
    public void changeStatus(@RequestParam Long reportID, String status){
        feedbackService.updateService(reportID, status);
        if(reportService.getReport(reportID).getUserID()==null) return;
        Users user = usersService.fetch(reportService.getReport(reportID).getUserID());
        try {
            emailService.sendRequestStatusChange(user.getEmail(),reportID, status);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/groupReports")
    public void groupReports(@RequestBody Long groupLeaderId, List<Long> groupMembers){
        if(groupLeaderId == null)
            throw new IllegalArgumentException("group leader id is null");
        Report groupLeader;
        try {
             groupLeader = reportService.getReport(groupLeaderId);
        }catch (com.progi.ostecenja.server.service.EntityMissingException e){
            throw new IllegalArgumentException("group leader not found exception");
        }
        reportService.groupReports(groupLeader, groupMembers);
    }

    public Feedback getStatus(@RequestParam long reportId){
        return feedbackService.getLatest(reportId);
    }

    @DeleteMapping("/delete")
    public void deleteReport(@RequestParam Long repotId){
        reportService.delete(repotId);
    }

    @PostMapping("/filtered")
    public List<Report> getReportsByFilter(@RequestBody ReportFilterDto reportFilterDto){
        return reportService.getReportsByFilter(reportFilterDto);
    }
    @GetMapping("/user/{userID}")
    public  ReportByStatusDTO getReportsByUserId(@PathVariable Long userID){
        return reportService.getReportsByUserId(userID);
    }
    @PostMapping("/statistic")
    public StatisticDTO getReportStatistic(@RequestBody ReportFilterDto reportFilterDto){
        return reportService.getReportStatistic(reportFilterDto);
    }

    @GetMapping("/status/{status}")
    public List<Report> getReportsByStatus(HttpSession session,@PathVariable String status){
      return reportService.getReportsByStatus(status, (Long) session.getAttribute("OFFICE"));
        //  return reportService.getReportsByStatus(status,52l);
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

@Getter
class ReportDTO {
    String reportHeadline;
    double lat;
    double lng;
    long categoryID;

    public void setReportHeadline(String reportHeadline) {
        this.reportHeadline = reportHeadline;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setCategoryID(long categoryID) {
        this.categoryID = categoryID;
    }
}
@Getter
class ReportImage{
    private Report report;
    private List<Image> images;
    public ReportImage(Report report, List<Image> images) {
        this.report = report;
        this.images = images;
    }
}

@Getter
class ReportCategory {
    private Report report;
    private Category category;
    public ReportCategory(Report report, Category category) {
        this.report = report;
        this.category = category;
    }
}

