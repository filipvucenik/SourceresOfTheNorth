package com.progi.ostecenja.server.service;

import com.progi.ostecenja.server.dto.ReportFilterDto;
import com.progi.ostecenja.server.repo.Report;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface ReportService {

    List<Report> listAllforUsers(long userID);
    List<Report> listAllforOffice(long cityOfficeID);
    Report createReport(Report report);

    List<Report> listAllUnhandled();

    Report getReport(Long reportID);

    List<String> getHeadlines();


     List<Report> getReportsByFilter(ReportFilterDto reportFilterDto);

    Optional<Report> findByUserId(Long reportID);

    List<Report> getReportsByUserId(Long userID);

    List<Report> listAll();
}

