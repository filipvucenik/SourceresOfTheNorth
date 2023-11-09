package com.progi.ostecenja.server.repo;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class FeedbackID implements Serializable {
    private Long groupID;
    private String status;

    public FeedbackID(Long groupID, String status) {
        this.groupID = groupID;
        this.status = status;
    }

    public FeedbackID() {

    }
    public Long getGroupID() {
        return groupID;
    }

    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        return groupID.hashCode() + status.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FeedbackID other){
            return this.status.equals(other.status) && this.groupID.equals(other.groupID);
        }
        return false;
    }
}
