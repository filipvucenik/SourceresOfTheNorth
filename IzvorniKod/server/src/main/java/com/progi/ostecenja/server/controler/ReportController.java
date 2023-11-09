package com.progi.ostecenja.server.controler;

import com.progi.ostecenja.server.repo.Report;
import com.progi.ostecenja.server.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @GetMapping
    public List<Report> listAllReports(){
        return reportService.listAll();
    }
    @PostMapping
    public Report createReport(@RequestBody Report report){
        return reportService.createReport(report);
    }
    @GetMapping("/{id}")

    @PostMapping("/{id}")

}
