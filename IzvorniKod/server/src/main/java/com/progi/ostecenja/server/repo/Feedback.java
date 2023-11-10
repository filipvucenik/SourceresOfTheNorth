package com.progi.ostecenja.server.repo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import java.sql.Timestamp;

@Entity(name ="Feedback")
public class Feedback {
    /*
    @Id
    @JoinColumn(name ="groupID")
    private Long groupID;
    */

    @Id
    private FeedbackID key;

    private Timestamp changeTS;

    public Feedback(Long groupID, String status, Timestamp changeTS) {
        this.key = new FeedbackID(groupID, status);
        this.changeTS = changeTS;
    }

    public Feedback(){

    }

    public Long getGroupID() {
        return key.getGroupID();
    }

    public void setGroupID(Long groupID) {
        key.setGroupID(groupID);
    }


    public String getStatus() {
        return key.getStatus();
    }

    public void setStatus(String status) {
        key.setStatus(status);
    }

    public Timestamp getChangeTS() {
        return changeTS;
    }

    public void setChangeTS(Timestamp changeTS) {
        this.changeTS = changeTS;
    }
}
