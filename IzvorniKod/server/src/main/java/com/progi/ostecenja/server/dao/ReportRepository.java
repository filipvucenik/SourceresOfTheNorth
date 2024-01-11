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
    String query = "SELECT r,f FROM Report r JOIN Feedback f ON r.reportID=f.key.groupID  WHERE " +
            "(:categoryID IS NULL AND :status IS NULL AND :radius IS NOT NULL AND " +
            "(:lat NOT BETWEEN -90.0 AND 90.0) AND (:lng NOT BETWEEN -180.0 AND 180.0) AND " +
            "CAST(:startDate AS TIMESTAMP) IS NULL AND CAST(:endDate AS TIMESTAMP) IS NULL) OR"+

            "(:categoryID IS NOT NULL AND r.categoryID = :categoryID) OR " +
            "(:status IS NOT NULL AND f.key.status=:status) OR "+
            "CASE " +
            "WHEN (:radius IS NOT NULL AND (:lat BETWEEN -90.0 AND 90.0) AND (:lng BETWEEN -180.0 AND 180.0)) THEN TRUE " +
            "ELSE " +
            "6371 * acos(" +
            "sin(radians(:lat)) * sin(radians(r.lat)) + " +
            "cos(radians(:lat)) * cos(radians(r.lat)) * cos(radians(:lng) - radians(r.lng))" +
            ") <=  + :radius END OR " +
            "((CAST(:startDate AS TIMESTAMP) IS NOT NULL AND CAST(:endDate AS TIMESTAMP) IS NULL) AND r.reportTS >= :startDate) OR " +
            "((CAST(:endDate AS TIMESTAMP) IS NOT NULL AND CAST(:startDate AS TIMESTAMP) IS NULL) AND r.reportTS <= :endDate) OR " +
            "((CAST(:endDate AS TIMESTAMP) IS NOT NULL AND CAST(:startDate AS TIMESTAMP) IS NOT NULL) AND r.reportTS BETWEEN CAST(:startDate AS TIMESTAMP) AND CAST(:endDate AS TIMESTAMP))";
    @Query(query)
    List<Report> findByReportAttributes(
            @Param("categoryID") Long categoryID,
            @Param("status") String status,
            @Param("radius") Double radius,
            @Param("lat") Double lat,
            @Param("lng") Double lng,
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate);
    List<Report> findByUserID(Long userID);


}
