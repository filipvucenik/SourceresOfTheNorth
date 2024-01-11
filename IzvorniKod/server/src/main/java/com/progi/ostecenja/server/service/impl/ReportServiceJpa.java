package com.progi.ostecenja.server.service.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.progi.ostecenja.server.dao.*;
import com.progi.ostecenja.server.dto.ReportFilterDto;
import com.progi.ostecenja.server.repo.*;
import com.progi.ostecenja.server.service.EntityMissingException;
import com.progi.ostecenja.server.service.FeedbackService;
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

    @Autowired
    FeedbackService feedbackService;

    @Override
    public List<Report> listAll(){
        return reportRepo.findAll();
    }

    @Override
    public List<Report> listAllforUsers(long userID) {
        return reportRepo.findAll().stream().filter(r -> r.getUserID().equals(userID)).toList();
    }

    @Override
    public List<Report> listAllforOffice(long cityOfficeID) {
        final List<Long> categories = categoryRepo.findAll().stream()
                .filter(c -> c.getCityOfficeID().equals(cityOfficeID))
                .mapToLong(Category::getCategoryID)
                .boxed().toList();
        return reportRepo.findAll().stream().filter(r -> categories.contains(r.getCategoryID())).toList();
    }

    @Override
    public Report createReport(Report report) {
        return reportRepo.save(report);
    }

    // TODO provjeriti tocno kak se stanje zove
    @Override
    public List<Report> listAllUnhandled() {

        List<Report> ret = reportRepo.findAll().stream().filter(r -> {
            Long id = r.getReportID();
            return !feedbackService.existsFeedback(id, "obraden");
        }).toList();

        return ret;
    }

    @Override
    public Optional<Report> findByUserId(Long reportID) {
        return reportRepo.findById(reportID);
    }

    @Override
    public List<String> getHeadlines() {
        return reportRepo.findAll().stream().map(Report::getReportHeadline).toList();
    }

    @Override
    public Report getReport(Long reportID) {
        return findByUserId(reportID).orElseThrow(
                () -> new EntityMissingException(Report.class, reportID)
        );
    }

    @Override
    public List<Report> getReportsByFilter(ReportFilterDto reportFilterDto) {
        if(reportFilterDto.getRadius()== null){
            reportFilterDto.setRadius(0.);
            reportFilterDto.setLng(200.);
            reportFilterDto.setLat(100.);
           // System.out.println(reportFilterDto.toPrint());
        }
        return
                reportRepo.findByReportAttributes(reportFilterDto.getCategoryId(), reportFilterDto.getStatus(),
                reportFilterDto.getRadius(),reportFilterDto.getLat(),reportFilterDto.getLng(), reportFilterDto.getStartDate(),
                reportFilterDto.getEndDate());

    }
    @Override
    public List<Report> getReportsByUserId(Long userID){
       return reportRepo.findByUserID(userID);
    }

    @Override
    public void groupReports(Report groupLeader, List<Report> members) {
        for(Report member: members){
            member.setGroup(groupLeader);
            reportRepo.save(member);
        }
    }

    @Override
    public void delete(long reportId) {
        Report toBeDeleted = reportRepo.getReferenceById(reportId);
        reportRepo.delete(toBeDeleted);
        List<Report> members = reportRepo.findAll().stream().filter(r->r.getReportID().equals(reportId)).toList();
        if(!members.isEmpty())
            reportRepo.deleteAll(members);
    }
}
