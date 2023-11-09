package com.progi.ostecenja.server.controler;

import com.progi.ostecenja.server.repo.Report;
import com.progi.ostecenja.server.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    ReportService reportService;
    @GetMapping("/filtered") // /filtered?categoryID=value1&TSbegin=value2&TSend=value3&status=value4&location=value5&locationRadius=value6
    public List<Report> listFilteredReports(
            @RequestParam(name = "categoryID", required = false) Long categoryID,
            @RequestParam(name = "TSbegin", required = false) Timestamp TSbegin,
            @RequestParam(name = "TSend", required = false) Timestamp TSend,
           // @RequestParam(name = "status", required = false) String status,@RequestParam(name = "locationRadius", required = false) String locationRadius
            @RequestParam(name = "location", required = false) String location) {


        return reportService.findByAttributes(categoryID,TSbegin,TSend,location);
    }
}
