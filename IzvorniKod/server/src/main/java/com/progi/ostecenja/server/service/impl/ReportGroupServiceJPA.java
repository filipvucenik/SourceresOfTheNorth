package com.progi.ostecenja.server.service.impl;

import com.progi.ostecenja.server.dao.ReportGroupRepository;
import com.progi.ostecenja.server.repo.ReportGroup;
import com.progi.ostecenja.server.service.ReportGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportGroupServiceJPA implements ReportGroupService {
    @Autowired
    ReportGroupRepository reportGroupRepository;

    @Override
    public void createReportGroup(Long reportGroup) {
        reportGroupRepository.save(new ReportGroup(reportGroup));
    }
}
