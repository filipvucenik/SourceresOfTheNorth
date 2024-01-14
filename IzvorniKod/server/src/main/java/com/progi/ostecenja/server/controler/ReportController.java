package com.progi.ostecenja.server.controler;

import com.progi.ostecenja.server.Utils.Pair;
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
@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true")
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

    @Autowired
    private JsonService jsonService;

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
    public List<ReportCategoryImage> groupReport(@RequestParam Long categoryID, @RequestParam Double lat, @RequestParam Double lng, HttpSession session){

        List<Report> reportList = reportService.listAll();
        List<Report> returnList = new ArrayList<Report>();
        for(Report rp: reportList){
            if(rp.getCategoryID()==null || rp.getLat()==null || rp.getLng()==null) continue;
            if(rp.getCategoryID().equals(categoryID) && calculateDistance(rp.getLat(),rp.getLng(),lat,lng)<=0.2){
                double dist = calculateDistance(rp.getLat(),rp.getLng(),lat,lng);
                returnList.add(rp);
            }
        }
        Map<Long, Category> categories =categoryService.listAll().stream().collect(Collectors.toMap(Category::getCategoryID, c -> c));

        List<ReportCategoryImage> ret = new ArrayList<>();
        for (Report rep: returnList){
            List<Image> images = imageService.listAllId(rep.getReportID());
            ret.add(new ReportCategoryImage(rep, categories.get(rep.getCategoryID()), images.isEmpty()?null:images.get(0)));
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
    public Report createReport(@RequestParam(required = false) Long reportID, @RequestParam(required = false) String reportHeadline,
                               @RequestParam(required = false) Double lat, @RequestParam(required = false) Double lng,
                               @RequestParam(required = false) String description, @RequestParam(required = false) Timestamp reportTS,
                               @RequestParam(required = false) Long userID, @RequestParam(required = false) Report group,
                               @RequestParam(required = false) Long categoryID, HttpSession session, List<MultipartFile> images, String address){
        Report report = new Report(null, reportHeadline, lat, lng, description, reportTS, userID, group, categoryID);
        Timestamp timestamp = reportTS;
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

        if(userId!=null){
            Users user = usersService.fetch(userId);
            try {
                emailService.sendRequestSubmittedEmail(user.getEmail(), saved.getReportID());
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
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

        jsonService.addToJsonFile(new Pair<>(saved.getLat(), saved.getLng()), address);

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

    @PutMapping("/updateStatus")
    public void changeStatus(@RequestParam("reportID") Long reportID, @RequestParam("status") String status) {
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
    public void groupReports(@RequestParam("mainReportID") Long groupLeaderId, @RequestParam("reports") List<Long> groupMembers){
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

    @GetMapping("/addresses")
    public Map<Pair<Double, Double>, String> getAddresses(){
        return jsonService.readJsonFile();
    }

    @PutMapping("/changeOffice")
    public void changeOffice(@RequestParam("CatID") Long categoryId, @RequestParam("reports") List<Long> reportIds){
        reportService.changeOffice(categoryId, reportIds);
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
@Getter
class ReportCategoryImage{
    private Report report;
    private Category category;
    private Image image;
    public ReportCategoryImage(Report report, Category category, Image image){
        this.report=report;
        this.category=category;
        this.image=image;
    }
}