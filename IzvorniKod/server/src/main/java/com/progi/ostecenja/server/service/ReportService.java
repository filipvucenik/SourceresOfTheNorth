package com.progi.ostecenja.server.service;

import com.progi.ostecenja.server.repo.Report;

import java.util.List;

public interface ReportService {

    List<Report> listAll();
    Report createReport(Report report);
}
