package com.progi.ostecenja.server.service.impl;

import com.progi.ostecenja.server.dao.ImageRepository;
import com.progi.ostecenja.server.dao.ReportRepository;
import com.progi.ostecenja.server.dao.UsersRepository;
import com.progi.ostecenja.server.repo.Image;
import com.progi.ostecenja.server.repo.Report;
import com.progi.ostecenja.server.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

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
    public List<Report> findByAttributes(@Param("categoryID") Long categoryID,
                                  @Param("TSbegin") Timestamp TSbegin,
                                  @Param("TSend") Timestamp TSend,
                                  @Param("location") String location){
       return reportRepo.findByAttributes(categoryID,TSbegin,TSend,location);
    }

    @Override
    public Report getReport(Long reportID) {
        return reportRepo.getReferenceById(reportID);
    }

}
