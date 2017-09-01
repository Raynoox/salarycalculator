package dg.projects.salarycalculator.services.Salary;

import dg.projects.salarycalculator.dto.CalculateSalaryDTO;
import dg.projects.salarycalculator.dto.RateDTO;
import dg.projects.salarycalculator.enums.CurrencyEnum;
import dg.projects.salarycalculator.services.Country.CountryService;
import dg.projects.salarycalculator.services.ExchangeRates.ExchangeRates;
import dg.projects.salarycalculator.services.Taxes.TaxesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
public class SalaryServiceImpl extends AbstractSalaryService {

    private final ExchangeRates exchangeRates;
    private final CountryService countryService;
    private final TaxesService taxesService;
    private final Long DAYS_IN_MONTH;
    @Autowired
    public SalaryServiceImpl(ExchangeRates exchangeRates, CountryService countryService, TaxesService taxesService, @Value("${salarycalculator.daysinmonth}") Long days) {
        this.exchangeRates = exchangeRates;
        this.countryService = countryService;
        this.taxesService = taxesService;
        this.DAYS_IN_MONTH = days;
    }

    @Override
    public Double calculateSalary(CalculateSalaryDTO dto) {
        dto.setCountry(countryService.getCountryByName(dto.getCountryName()).orElseThrow(CountryNotFoundException::new));
        return calculate(dto);
    }

    @Override
    protected CalculateSalaryDTO applyTaxes(CalculateSalaryDTO dto) {
        dto.setSalary(taxesService.applyTaxesForCountry(dto.getSalary(), dto.getCountry()));
        return roundSalary(dto);
    }

    @Override
    protected CalculateSalaryDTO calculateMonthlySalary(CalculateSalaryDTO dto) {
        dto.setSalary(dto.getSalary()*DAYS_IN_MONTH);
        return roundSalary(dto);
    }

    @Override
    protected CalculateSalaryDTO exchangeMoney(CalculateSalaryDTO dto) {
        if(shouldExchange(dto)) {
            Optional<RateDTO> rate = exchangeRates.getExchangeRate(dto.getCountry().getCurrency());
            dto.setSalary(getExchangedMoney(dto, rate));
        }
        return roundSalary(dto);
    }
    private CalculateSalaryDTO roundSalary(CalculateSalaryDTO dto) {
        dto.setSalary(BigDecimal
                .valueOf(dto.getSalary())
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue());
        return dto;
    }
    private boolean shouldExchange(CalculateSalaryDTO dto) {
        return !CurrencyEnum.PLN.equals(dto.getCountry().getCurrency());
    }
    private Double getExchangedMoney(CalculateSalaryDTO dto, Optional<RateDTO> rate) {
        return dto.getSalary()*rate.orElseThrow(RateNotFoundException::new).getMid();
    }
}
