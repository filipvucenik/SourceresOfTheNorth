package com.progi.ostecenja.server.repo;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

public class Image {

    @Id
    private Long imageID;

    @JoinColumn(name ="reportID")
    private Long reportID;

    private String URL;

    public Image (){

    }
    public Image(Long imageID, Long reportID, String URL) {
        this.imageID = imageID;
        this.reportID = reportID;
        this.URL = URL;
    }

    public Long getImageID() {
        return imageID;
    }

    public void setImageID(Long imageID) {
        this.imageID = imageID;
    }

    public Long getReportID() {
        return reportID;
    }

    public void setReportID(Long reportID) {
        this.reportID = reportID;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
