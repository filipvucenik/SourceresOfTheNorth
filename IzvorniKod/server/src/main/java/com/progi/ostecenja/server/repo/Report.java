package com.progi.ostecenja.server.repo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity(name = "Report")
public class Report {
    @Id @GeneratedValue
    private Long reportID;

    @Column
    private String location;

}
