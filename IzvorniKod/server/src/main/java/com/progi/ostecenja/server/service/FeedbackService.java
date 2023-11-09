package com.progi.ostecenja.server.service;

import com.progi.ostecenja.server.repo.Feedback;

import java.sql.Timestamp;

public interface FeedbackService {

    Feedback createFeedback(Long groupID, Long cityOfficeID, Timestamp changeTS);
}
