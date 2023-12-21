package com.progi.ostecenja.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.progi.ostecenja.server.repo.Report;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    String query = "SELECT r FROM Report r WHERE " +
            "(:categoryID IS NOT NULL AND r.categoryID = :categoryID) OR " +
            "(:radius IS NOT NULL) OR " +
            "(( CAST(:startDate AS TIMESTAMP)  IS NOT NULL AND CAST(:endDate AS TIMESTAMP) IS NULL) AND r.reportTS >= :startDate) OR " +
            "(( CAST(:endDate AS TIMESTAMP) IS NOT NULL AND CAST(:startDate AS TIMESTAMP) IS NULL) AND r.reportTS <= :endDate) OR " +
            "(( CAST(:endDate AS TIMESTAMP) IS NOT NULL AND CAST(:startDate AS TIMESTAMP) IS NOT NULL) AND r.reportTS BETWEEN CAST(:startDate AS TIMESTAMP)  AND CAST(:endDate AS TIMESTAMP)) OR " +
            "(:status IS NOT NULL)";
    @Query(query)
    List<Report> findByReportAttributes(
            @Param("categoryID") Long categoryID,
            @Param("status") String status,
            @Param("radius") String radius,
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate);


}
