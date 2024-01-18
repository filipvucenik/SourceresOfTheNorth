package com.progi.ostecenja.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StatisticDTO {
    private Integer reportCount;
    private  Double avgReportsByDay;

    private Integer reportWaitingCount;
    private Double reportWaitingShare;

    private Integer reportInProgressCount;
    private Double reportInProgressShare;

    private Integer reportSolvedCount;
    private Double reportSolvedShare;


    private String avgTimeWaiting;
    private  String avgTimeInProgress;

}
