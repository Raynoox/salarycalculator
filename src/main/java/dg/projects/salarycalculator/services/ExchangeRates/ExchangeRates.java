package dg.projects.salarycalculator.services;

import dg.projects.salarycalculator.dto.RateDTO;
import dg.projects.salarycalculator.enums.CurrencyEnum;

import java.util.Optional;


public interface ExchangeRates {
    Optional<RateDTO> getExchangeRate(CurrencyEnum currency);
}
