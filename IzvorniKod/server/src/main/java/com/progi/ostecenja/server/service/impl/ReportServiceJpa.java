package com.progi.ostecenja.server.service.impl;

import com.progi.ostecenja.server.dao.ImageRepository;
import com.progi.ostecenja.server.dao.ReportRepository;
import com.progi.ostecenja.server.dao.UsersRepository;
import com.progi.ostecenja.server.repo.Image;
import com.progi.ostecenja.server.repo.Report;
import com.progi.ostecenja.server.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceJpa implements ReportService {
    @Autowired
    ReportRepository reportRepo;


    @Override
    public List<Report> listAll(){return reportRepo.findAll();}
    @Override
    public  Report createReport(Report report){
       return reportRepo.save(report);
    }

    @Override
    public Report getReport(Long reportID) {
        return reportRepo.getReferenceById(reportID);
    }

}
