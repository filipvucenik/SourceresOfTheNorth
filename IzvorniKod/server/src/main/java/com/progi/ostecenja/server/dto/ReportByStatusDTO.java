package com.progi.ostecenja.server.dto;

import com.progi.ostecenja.server.repo.Report;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportByStatusDTO {
    private Report report;
    private Integer waitingCount;
    private Integer inProgressCount;
    private Integer solvedCount;
}
