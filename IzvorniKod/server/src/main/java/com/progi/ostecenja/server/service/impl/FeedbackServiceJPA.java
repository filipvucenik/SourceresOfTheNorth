package com.progi.ostecenja.server.service.impl;

import com.progi.ostecenja.server.dao.FeedbackRepository;
import com.progi.ostecenja.server.repo.Feedback;
import com.progi.ostecenja.server.repo.FeedbackID;
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
    public Feedback createFeedback(Long groupID, Timestamp changeTS) {
        return feedbackRepository.save(new Feedback(groupID, "neobrađeno", changeTS));
    }

    @Override
    public void updateService(Long groupID, String stanje) {
        FeedbackID current = new FeedbackID(groupID, stanje);
        if(feedbackRepository.existsById(current)){
            throw new IllegalArgumentException("State for this group is already updated");
        }
        feedbackRepository.save(new Feedback(groupID, stanje, Timestamp.valueOf(LocalDateTime.now())));
    }

    @Override
    public Feedback getFeedback(Long groupID, String stanje) {
        if(existsFeedback(groupID, stanje))
            return feedbackRepository.getReferenceById(new FeedbackID(groupID, stanje));
        else
            throw new IllegalArgumentException("Feedback: "+ groupID+" "+ stanje +" does not exists");
    }

    @Override
    public boolean existsFeedback(Long groupID, String stanje) {
        return feedbackRepository.existsById(new FeedbackID(groupID, stanje));
    }
}
