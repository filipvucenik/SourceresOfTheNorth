package com.progi.ostecenja.server.service.impl;

import com.progi.ostecenja.server.dao.FeedbackRepository;
import com.progi.ostecenja.server.dao.ReportRepository;
import com.progi.ostecenja.server.repo.Feedback;
import com.progi.ostecenja.server.repo.FeedbackID;
import com.progi.ostecenja.server.repo.Report;
import com.progi.ostecenja.server.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedbackServiceJPA implements FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Override
    public Feedback createFeedback(Long groupID, Timestamp changeTS) {
        return feedbackRepository.save(new Feedback(groupID, "neobraden", changeTS));
    }

    @Override
    public void updateService(Long reportId, String stanje) {
        FeedbackID current = new FeedbackID(reportId, stanje);
        if(!reportRepository.existsById(reportId)){
            throw new IllegalArgumentException("Report does not exist");
        }
        if(feedbackRepository.existsById(current)){
            throw new IllegalArgumentException("State for this group is already updated");
        }
        List<Report> groupMembers = reportRepository.findAll().stream().filter(r->r.getGroup() != null).filter(r->r.getGroup().getReportID().equals(reportId)).toList();

        for(Report member : groupMembers){
            feedbackRepository.save( new Feedback(member.getReportID(), stanje, Timestamp.valueOf(LocalDateTime.now())));
        }
        feedbackRepository.save(new Feedback(reportId, stanje, Timestamp.valueOf(LocalDateTime.now())));
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

    @Override
    public Feedback getLatest(Long reportID) {
        List<String> stanja = List.of(new String[]{"neobraden","uProcesu","obraden"});
        Feedback ret = null;
        for(String stanje: stanja){
            FeedbackID id = new FeedbackID(reportID, stanje);
            if(feedbackRepository.existsById(id))
                ret = feedbackRepository.getReferenceById(id);
        }
        return ret;
    }
    @Override
    public List<Feedback> getAll(){
        return feedbackRepository.findAll();
    }
}
