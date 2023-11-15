package com.progi.ostecenja.server.repo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity(name="ReportGroup")
public class ReportGroup {
    @Id @GeneratedValue
    private Long groupID;

    public Long getGroupID() {
        return groupID;
    }
    public ReportGroup(){}
    public ReportGroup(Long groupID) {
        this.groupID = groupID;
    }
}
