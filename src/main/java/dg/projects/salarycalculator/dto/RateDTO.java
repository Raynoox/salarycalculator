package dg.projects.salarycalculator.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RateDTO {
    private Date effectiveDate;
    private Double mid;
    private String no;
}