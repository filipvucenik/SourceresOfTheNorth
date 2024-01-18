package com.progi.ostecenja.server.dto;

import com.progi.ostecenja.server.repo.Report;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportByStatusDTO {
    private List<ReportFeedbackJoin> reports;
    private Integer waitingCount;
    private Integer inProgressCount;
    private Integer solvedCount;
}
