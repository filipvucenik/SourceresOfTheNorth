package com.progi.ostecenja.server.service;

import com.progi.ostecenja.server.repo.Feedback;

import java.sql.Timestamp;

public interface FeedbackService {

    Feedback createFeedback(Long groupID, Timestamp changeTS);

    void updateService(Long groupID, String stanje);
}
