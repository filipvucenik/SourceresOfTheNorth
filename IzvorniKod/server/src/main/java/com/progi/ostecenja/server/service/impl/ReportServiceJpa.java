package com.progi.ostecenja.server.service.impl;

import com.progi.ostecenja.server.dao.*;
import com.progi.ostecenja.server.repo.Category;
import com.progi.ostecenja.server.repo.CityOffice;
import com.progi.ostecenja.server.repo.Image;
import com.progi.ostecenja.server.repo.Report;
import com.progi.ostecenja.server.service.EntityMissingException;
import com.progi.ostecenja.server.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportServiceJpa implements ReportService {
    @Autowired
    ReportRepository reportRepo;

    @Autowired
    CategoryRepository categoryRepo;
    @Override
    public List<Report> listAllforUsers(long userID) {
        return reportRepo.findAll().stream().filter(r-> r.getUserID().equals(userID)).toList();
    }

    @Override
    public List<Report> listAllforOffice(long cityOfficeID) {
        final List<Long> categories = categoryRepo.findAll().stream()
                .filter(c -> c.getCityOfficeID().equals(cityOfficeID))
                .mapToLong(Category::getCategoryID)
                .boxed().toList();
        return reportRepo.findAll().stream().filter(r-> categories.contains(r.getCategoryID())).toList();
    }

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
    public Optional<Report> findByUserId(Long reportID) {
        return reportRepo.findById(reportID);
    }

    @Override
    public Report getReport(Long reportID) {
        return findByUserId(reportID).orElseThrow(
                () -> new EntityMissingException(Report.class, reportID)
        );
    }

}
