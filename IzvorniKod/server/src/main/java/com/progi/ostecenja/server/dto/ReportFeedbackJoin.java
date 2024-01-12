package com.progi.ostecenja.server.dto;

import com.progi.ostecenja.server.repo.Category;
import com.progi.ostecenja.server.repo.Feedback;
import com.progi.ostecenja.server.repo.Report;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportFeedbackJoin {
    private Report report;
    private Feedback feedback;
    private Category category;

}