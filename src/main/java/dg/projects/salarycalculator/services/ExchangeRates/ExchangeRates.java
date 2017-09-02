package dg.projects.salarycalculator.services.ExchangeRates;

import dg.projects.salarycalculator.dto.RateDTO;
import dg.projects.salarycalculator.enums.CurrencyEnum;


public interface ExchangeRates {
    RateDTO getExchangeRate(CurrencyEnum currency);
}
