package com.progi.ostecenja.server.service;

import com.progi.ostecenja.server.dto.ReportByStatusDTO;
import com.progi.ostecenja.server.dto.ReportFilterDto;
import com.progi.ostecenja.server.dto.StatisticDTO;
import com.progi.ostecenja.server.repo.Report;

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

    ReportByStatusDTO getReportsByUserId(Long userID);
    List<Report> listAll();
    void groupReports(Report groupLeader, List<Long> members);
    void delete(long repot);
    StatisticDTO getReportStatistic(ReportFilterDto reportFilterDto);

    List<Report> getReportsByStatus(String status, Long officeID);
}

