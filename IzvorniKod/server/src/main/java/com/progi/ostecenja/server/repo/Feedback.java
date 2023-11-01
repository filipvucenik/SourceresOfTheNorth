package com.progi.ostecenja.server.repo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import java.sql.Timestamp;

@Entity(name ="Feedback")
public class Feedback {
    @Id
    @JoinColumn(name ="groupID")
    private Long groupID;
    @Id
    @JoinColumn(name = "cityOfficeID")
    private Long cityOfficeID;

    private String status;

    private Timestamp changeTS;

    public Feedback(Long groupID, Long cityOfficeID, String status, Timestamp changeTS) {
        this.groupID = groupID;
        this.cityOfficeID = cityOfficeID;
        this.status = status;
        this.changeTS = changeTS;
    }

    public Feedback(){

    }

    public Long getGroupID() {
        return groupID;
    }

    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }

    public Long getCityOfficeID() {
        return cityOfficeID;
    }

    public void setCityOfficeID(Long cityOfficeID) {
        this.cityOfficeID = cityOfficeID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getChangeTS() {
        return changeTS;
    }

    public void setChangeTS(Timestamp changeTS) {
        this.changeTS = changeTS;
    }
}
