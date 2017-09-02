package dg.projects.salarycalculator.dto;

import dg.projects.salarycalculator.enums.CurrencyEnum;
import lombok.Data;

import java.util.List;
@Data
public class NbpRateDTO {
    private CurrencyEnum code;
    private String currency;
    private List<RateDTO> rates;

    public NbpRateDTO() {

    }

    @Data
    private class Rates {
        private String effectiveDate;
        private Double mid;
        private String no;
    }
}
