package com.progi.ostecenja.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.progi.ostecenja.server.repo.Report;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    /*
    @Query("SELECT r FROM Report r " +
            "WHERE (:categoryID IS NULL OR r.categoryID = :categoryID) " +
            "  AND (:TSbegin IS NULL OR r.reportTS >= :TSbegin ) " +
            "  AND (:TSend IS NULL OR r.reportTS <= :TSend) " +
            "  AND (:location IS NULL OR r.location=location"
            )
    List<Report> findByAttributes(@Param("categoryID") Long categoryID,
                                  @Param("TSbegin") Timestamp TSbegin,
                                  @Param("TSend") Timestamp TSend,
                                  @Param("location") String location);
     */

}
