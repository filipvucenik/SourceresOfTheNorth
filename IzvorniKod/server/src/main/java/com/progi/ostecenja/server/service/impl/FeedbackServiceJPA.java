package com.progi.ostecenja.server.service.impl;

import com.progi.ostecenja.server.dao.FeedbackRepository;
import com.progi.ostecenja.server.repo.Feedback;
import com.progi.ostecenja.server.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class FeedbackServiceJPA implements FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Override
    public Feedback createFeedback(Long groupID, Long cityOfficeID, Timestamp changeTS) {
        return feedbackRepository.save(new Feedback(groupID, "neobrađeno", cityOfficeID, changeTS));
    }

}
