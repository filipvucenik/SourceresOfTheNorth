package com.progi.ostecenja.server.repo;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity(name = "Report")
public class Report {
    @Id @GeneratedValue
    private Long reportID;
    @Column
    private String reportHeadline;
    @Column
    private String location;

    @Column
    private String description;

    @Column
    private Timestamp reportTS;

    @Column
    private String trStatus;

    @JoinColumn(name="userID")
    private Long userID;

    @JoinColumn(name="groupID")
    private Long groupID;

    @JoinColumn(name="categoryID")
    private Long categoryID;

    public void setReportHeadline(String reportHeadline) {
        this.reportHeadline = reportHeadline;
    }

    public String getReportHeadline() {
        return reportHeadline;
    }

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

    public void setReportID(Long reportID) {
        this.reportID = reportID;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReportTS(Timestamp reportTS) {
        this.reportTS = reportTS;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }

    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }

    public String getTrStatus() {
        return trStatus;
    }

    public void setTrStatus(String trStatus) {
        this.trStatus = trStatus;
    }

    public Report(){
    }
    public Report(Long reportID,String reportHeadline, String location, String description, Timestamp reportTS, Long userID, Long groupID, Long categoryID) {
        this.reportID = reportID;
        this.reportHeadline=reportHeadline;
        this.location = location;
        this.description = description;
        this.reportTS = reportTS;
        this.userID = userID;
        this.groupID = groupID;
        this.categoryID = categoryID;
    }
}
