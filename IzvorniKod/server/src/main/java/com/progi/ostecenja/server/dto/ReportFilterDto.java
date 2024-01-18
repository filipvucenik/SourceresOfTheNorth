package com.progi.ostecenja.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportFilterDto {
    private Long categoryId;
    private String status;
    private Double radius;
    private Double lat;
    private Double lng;
    private Timestamp startDate;
    private Timestamp endDate;


    public String toPrint(){
        return "categoryId: " + this.categoryId + " , status: " + this.status + " radius: "
                + this.radius + " lat:"+ this.lat + " lng:" + this.lng + " startDate:" + this.startDate + " endDate:" + this.endDate;
    }
}
