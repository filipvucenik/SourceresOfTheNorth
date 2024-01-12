package com.progi.ostecenja.server.dao;

import com.progi.ostecenja.server.dto.ReportFeedbackJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import com.progi.ostecenja.server.repo.Report;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("SELECT DISTINCT new com.progi.ostecenja.server.dto.ReportFeedbackJoin(r,f) FROM Report r JOIN Feedback f ON r.reportID=f.key.groupID  " +
            "WHERE  f.changeTS IN (" +
            "                      SELECT MAX(fed.changeTS) " +
            "                      FROM Feedback fed"  +
            "                      WHERE fed.key.groupID = f.key.groupID" +
            "                     ) "
          )
    List<ReportFeedbackJoin> findByReportAttributes();

    @Query(
            "SELECT new com.progi.ostecenja.server.dto.ReportFeedbackJoin(r,f,c) FROM Report r JOIN Feedback f ON f.key.groupID = r.reportID " +
                    "JOIN Category c ON c.categoryID = r.categoryID " +
                    "WHERE r.userID = :userID AND f.changeTS IN (" +
                    "    SELECT MAX(fed.changeTS) " +
                    "    FROM Feedback fed" +
                    "    WHERE fed.key.groupID = f.key.groupID" +
                    "   ) "
    )
    List<ReportFeedbackJoin> findByUserIDAndJoinWithFeedback(@Param("userID") Long userID);

    @Query(
            "SELECT DISTINCT r,f,co,cat FROM CityOffice co JOIN Category cat ON cat.cityOfficeID = co.cityOfficeId JOIN Report r ON r.categoryID=cat.categoryID " +
                    "JOIN Feedback f ON r.reportID=f.key.groupID " +
                    "WHERE :status = f.key.status AND cat.cityOfficeID = :cityOfficeID AND f.changeTS IN (" +
                    "    SELECT MAX(fed.changeTS) " +
                    "    FROM Feedback fed" +
                    "    WHERE fed.key.groupID = f.key.groupID" +
                    "   ) "
    )
    List<Report> findByOfficeIdAndStatus(@Param("cityOfficeID") Long cityOfficeID, @Param("status") String status);
}
