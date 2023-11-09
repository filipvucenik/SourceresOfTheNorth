package com.progi.ostecenja.server.repo;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class FeedbackID implements Serializable {
    private Long groupID;
    private Long citiOfficeID;

    public FeedbackID(Long groupID, Long citiOfficeID) {
        this.groupID = groupID;
        this.citiOfficeID = citiOfficeID;
    }

    public FeedbackID() {

    }

    public Long getGroupID() {
        return groupID;
    }

    public Long getCitiOfficeID() {
        return citiOfficeID;
    }

    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }

    public void setCitiOfficeID(Long citiOfficeID) {
        this.citiOfficeID = citiOfficeID;
    }

    @Override
    public int hashCode() {
        return groupID.hashCode() + citiOfficeID.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FeedbackID){
            FeedbackID other = (FeedbackID) obj;
            return this.citiOfficeID.equals(other.citiOfficeID) && this.groupID.equals(other.groupID);
        }
        return false;
    }
}
