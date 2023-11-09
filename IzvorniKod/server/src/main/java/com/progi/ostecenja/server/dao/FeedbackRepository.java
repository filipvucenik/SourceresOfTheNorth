package com.progi.ostecenja.server.dao;

import com.progi.ostecenja.server.repo.Feedback;
import com.progi.ostecenja.server.repo.FeedbackID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, FeedbackID> {
}
