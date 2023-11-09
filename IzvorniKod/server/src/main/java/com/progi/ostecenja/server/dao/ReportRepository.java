package com.progi.ostecenja.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.progi.ostecenja.server.repo.Report;
public interface ReportRepository extends JpaRepository<Report, Long> {

}
