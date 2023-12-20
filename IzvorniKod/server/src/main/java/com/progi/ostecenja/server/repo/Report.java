package com.progi.ostecenja.server.repo;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class Report {
    @Id
    @GeneratedValue
    private Long reportID;
    @Column
    private String reportHeadline;
    @Column
    private String location;

    @Column
    private String description;

    @Column
    private Timestamp reportTS;

    @JoinColumn
    private Long userID;
    @ManyToOne
    @JoinColumn (
            name = "group_reportID",
            referencedColumnName = "reportID"
    )
    private Report group;

    @JoinColumn
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

    public void setGroup(Report group) {
        this.group = group;
    }

    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }

    @JsonInclude(JsonInclude.Include.ALWAYS)
    public Report getGroup() {
        return group;
    }

    public Report(){
    }
    public Report(Long reportID,String reportHeadline, String location, String description, Timestamp reportTS, Long userID, Report group, Long categoryID) {
        this.reportID = reportID;
        this.reportHeadline=reportHeadline;
        this.location = location;
        this.description = description;
        this.reportTS = reportTS;
        this.userID = userID;
        this.group = group;
        this.categoryID = categoryID;
    }
}
