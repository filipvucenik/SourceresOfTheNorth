package com.progi.ostecenja.server.repo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@NoArgsConstructor
@Getter
@Entity(name ="Feedback")
public class Feedback {

    @Id
    private FeedbackID key;
    @Column
    private Timestamp changeTS;

    public Feedback(Long reportID, String status, Timestamp changeTS) {
        this.key = new FeedbackID(reportID, status);
        this.changeTS = changeTS;
    }

}
