package com.progi.ostecenja.server.service;

import com.progi.ostecenja.server.repo.Feedback;

import java.sql.Timestamp;
import java.util.List;

public interface FeedbackService {

    Feedback createFeedback(Long groupID, Timestamp changeTS);

    void updateService(Long groupID, String stanje);

    Feedback getFeedback(Long groupID, String stanje);

    boolean existsFeedback(Long groupID, String stanje);

     List<Feedback> getAll();
}
