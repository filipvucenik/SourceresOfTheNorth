package com.progi.ostecenja.server.dao;

import com.progi.ostecenja.server.repo.Feedback;
import com.progi.ostecenja.server.repo.FeedbackID;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, FeedbackID> {
    @Query(
            "SELECT f FROM Report r JOIN Feedback f ON f.key.groupID=r.reportID " +
                    "WHERE r.reportID = :reportID AND f.changeTS IN (" +
                    "    SELECT MAX(fed.changeTS) " +
                    "    FROM Feedback fed" +
                    "    WHERE fed.key.groupID = f.key.groupID" +
                    "   ) "
    )
     Feedback getLatest(@Param("reportID") Long reportID);
  /* pozovi ako se zezne feedback
   @Transactional
    @Modifying
    @Query("DELETE FROM Feedback f WHERE f.key.groupID NOT IN (SELECT r.reportID FROM Report r WHERE r.reportID = f.key.groupID)"
            )
     void deleteExtraFeedbacks();*/

    @Transactional
    @Modifying
    @Query("DELETE FROM Feedback f WHERE f.key.groupID =:reportID"
    )
    void deleteFeedbacksForReportWithID(@Param("reportID") long reportID);
}
