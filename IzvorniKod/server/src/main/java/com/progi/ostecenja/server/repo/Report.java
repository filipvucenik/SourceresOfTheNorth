package com.progi.ostecenja.server.repo;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;

import java.sql.Timestamp;

@Entity
public class Report {
    @Getter
    @Id
    @GeneratedValue
    private Long reportID;
    @Getter
    @Column
    private String reportHeadline;
    @Column
    private Double lat;
    @Column
    private Double lng;
    @Getter
    @Column
    private String description;

    @Getter
    @Column
    private Timestamp reportTS;

    @Getter
    @JoinColumn
    private Long userID;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn (
            name = "group_reportID",
            referencedColumnName = "reportID"
    )
    private Report group;

    @Getter
    @JoinColumn
    private Long categoryID;

    public Report(){
    }
    public Report(Long reportID,String reportHeadline, double lat, double lng, String description, Timestamp reportTS, Long userID, Report group, Long categoryID) {
        this.reportID = reportID;
        this.reportHeadline=reportHeadline;
        this.lat = lat;
        this.lng = lng;
        this.description = description;
        this.reportTS = reportTS;
        this.userID = userID;
        this.group = group;
        this.categoryID = categoryID;
    }

    public Long getReportID() {
        return reportID;
    }

    public void setReportID(Long reportID) {
        this.reportID = reportID;
    }

    public String getReportHeadline() {
        return reportHeadline;
    }

    public void setReportHeadline(String reportHeadline) {
        this.reportHeadline = reportHeadline;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getReportTS() {
        return reportTS;
    }

    public void setReportTS(Timestamp reportTS) {
        this.reportTS = reportTS;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Report getGroup() {
        return group;
    }

    public void setGroup(Report group) {
        this.group = group;
    }

    public Long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }
}
