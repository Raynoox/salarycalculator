package dg.projects.salarycalculator.services.Salary;

import dg.projects.salarycalculator.dto.CalculateSalaryDTO;
import dg.projects.salarycalculator.dto.RateDTO;
import dg.projects.salarycalculator.enums.CountryEnum;
import dg.projects.salarycalculator.enums.CurrencyEnum;
import dg.projects.salarycalculator.models.Country;
import dg.projects.salarycalculator.services.Country.CountryService;
import dg.projects.salarycalculator.services.ExchangeRates.ExchangeRates;
import dg.projects.salarycalculator.services.ExchangeRates.RateNotFoundException;
import dg.projects.salarycalculator.services.Taxes.TaxesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SalaryServiceImpl.class)
public class SalaryServiceImplTest {
    @MockBean
    ExchangeRates exchangeRate;
    @MockBean
    CountryService countryService;
    @MockBean
    TaxesService taxesService;
    @Autowired
    SalaryService salaryService;

    private final Long EXAMPLE_DAYS_IN_MONTH = 10L;
    private Double TAXES_SUM = 1000.0;
    private final Double INITIAL_SALARY = 10000.0;
    private final Double EXCHANGE_RATE = 4.2;
    private final Double AFTER_TAXES = 99000.0;
    private final Double AFTER_TAXES_FRACTION_UP = 99000.495000001;
    private final Double ROUNDED_UP = 99000.500000;
    private final Double AFTER_TAXES_FRACTION_DOWN = 99000.49499999;
    private final Double ROUNDED_DOWN = 99000.490000;
    private final String COUNTRY_NAME = "COUNTRY_NAME";
    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(salaryService, "DAYS_IN_MONTH", EXAMPLE_DAYS_IN_MONTH);

    }
    @Test(expected = RateNotFoundException.class)
    public void throwsNoRateFoundException() {
        CalculateSalaryDTO dto = createDto(CurrencyEnum.EUR);
        doThrow(new RateNotFoundException()).when(exchangeRate).getExchangeRate(dto.getCountry().getCurrency());
        when(countryService.getCountryByName(dto.getCountryName())).thenReturn(Optional.of(dto.getCountry()));

        salaryService.calculateSalary(dto);

        verify(exchangeRate, times(1)).getExchangeRate(dto.getCountry().getCurrency());
    }
    @Test(expected = CountryNotFoundException.class)
    public void throwsCountryNotFoundException() {
        CalculateSalaryDTO dto = createDto(CurrencyEnum.EUR);
        doThrow(new CountryNotFoundException()).when(countryService).getCountryByName(dto.getCountryName());

        salaryService.calculateSalary(dto);

        verify(countryService, times(1)).getCountryByName(dto.getCountryName());
    }
    @Test
    public void exchangeNotAppliedWhenPLNCurrency() {
        CalculateSalaryDTO dto = createDto(CurrencyEnum.PLN);
        when(countryService.getCountryByName(dto.getCountryName())).thenReturn(Optional.of(dto.getCountry()));
        when(taxesService.applyTaxesForCountry(dto.getSalary()*EXAMPLE_DAYS_IN_MONTH,dto.getCountry())).thenReturn(AFTER_TAXES);

        Double result = salaryService.calculateSalary(dto);

        assertEquals(AFTER_TAXES, result);
        verify(exchangeRate, times(0)).getExchangeRate(CurrencyEnum.PLN);
    }
    @Test
    public void exchangeAppliedWhenEURCurrency() {
        CalculateSalaryDTO dto = createDto(CurrencyEnum.EUR);
        when(countryService.getCountryByName(dto.getCountryName())).thenReturn(Optional.of(dto.getCountry()));
        RateDTO rateDTO = getRateDTO(EXCHANGE_RATE);
        when(taxesService.applyTaxesForCountry(dto.getSalary()*EXAMPLE_DAYS_IN_MONTH,dto.getCountry())).thenReturn(AFTER_TAXES);
        when(exchangeRate.getExchangeRate(CurrencyEnum.EUR)).thenReturn(rateDTO);
        Double result = salaryService.calculateSalary(dto);

        Double expectedSalary = (AFTER_TAXES)*(EXCHANGE_RATE);

        assertEquals(expectedSalary, result);
        verify(exchangeRate, times(1)).getExchangeRate(CurrencyEnum.EUR);
    }

    private RateDTO getRateDTO(Double rate) {
        RateDTO rateDTO = new RateDTO();
        rateDTO.setMid(rate);
        return rateDTO;
    }

    @Test
    public void testRounding() {
        testWhenSalaryThenReturn(AFTER_TAXES_FRACTION_DOWN, ROUNDED_DOWN);
        testWhenSalaryThenReturn(AFTER_TAXES_FRACTION_UP, ROUNDED_UP);
    }

    private void testWhenSalaryThenReturn(Double salaryAfterTaxes, Double expected) {
        CalculateSalaryDTO dto = createDto(CurrencyEnum.EUR);
        when(countryService.getCountryByName(dto.getCountryName())).thenReturn(Optional.of(dto.getCountry()));
        RateDTO rateDTO = getRateDTO(1.0);

        when(taxesService.applyTaxesForCountry(dto.getSalary()*EXAMPLE_DAYS_IN_MONTH,dto.getCountry())).thenReturn(salaryAfterTaxes);
        when(exchangeRate.getExchangeRate(CurrencyEnum.EUR)).thenReturn(rateDTO);
        Double result = salaryService.calculateSalary(dto);

        assertEquals(expected, result);
    }

    private Country createTestCountry(CurrencyEnum currencyEnum) {
        return new Country.Builder().country(CountryEnum.GERMANY).currency(currencyEnum).build();
    }
    private CalculateSalaryDTO createDto(CurrencyEnum currencyEnum) {
        Country country = createTestCountry(currencyEnum);
        CalculateSalaryDTO dto = new CalculateSalaryDTO();
        dto.setSalary(INITIAL_SALARY);
        dto.setCountryName(COUNTRY_NAME);
        dto.setCountry(country);
        return dto;
    }
}