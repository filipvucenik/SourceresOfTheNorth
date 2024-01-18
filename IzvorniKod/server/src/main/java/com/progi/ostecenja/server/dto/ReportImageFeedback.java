package com.progi.ostecenja.server.dto;

import com.progi.ostecenja.server.repo.Feedback;
import com.progi.ostecenja.server.repo.Image;
import com.progi.ostecenja.server.repo.Report;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ReportImageFeedback{
    private Report report;
    private List<Image> images;
    private Feedback feedback;
}
