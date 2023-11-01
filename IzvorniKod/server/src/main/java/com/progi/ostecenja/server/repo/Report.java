package com.progi.ostecenja.server.repo;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity(name = "Report")
public class Report {
    @Id @GeneratedValue
    private Long reportID;

    @Column
    private String location;

    @Column
    private String description;

    @Column
    private Timestamp reportTS;

    @JoinColumn(name="userID")
    private Long userID;

    @JoinColumn(name="groupID")
    private Long groupID;

    @JoinColumn(name="categoryID")
    private Long categoryID;

    public Long getReportID() {
        return reportID;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getReportTS() {
        return reportTS;
    }

    public Long getUserID() {
        return userID;
    }

    public Long getGroupID() {
        return groupID;
    }

    public Long getCategoryID() {
        return categoryID;
    }

    public Report(){
    }
    public Report(Long reportID, String location, String description, Timestamp reportTS, Long userID, Long groupID, Long categoryID) {
        this.reportID = reportID;
        this.location = location;
        this.description = description;
        this.reportTS = reportTS;
        this.userID = userID;
        this.groupID = groupID;
        this.categoryID = categoryID;
    }
}
