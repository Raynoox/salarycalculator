package dg.projects.salarycalculator.services.Country;

import dg.projects.salarycalculator.dao.CountryDao;
import dg.projects.salarycalculator.enums.CountryEnum;
import dg.projects.salarycalculator.enums.CurrencyEnum;
import dg.projects.salarycalculator.models.Country;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CountryServiceImpl.class)
public class CountryServiceImplTest {

    private static final String COUNTRY_NAME = "COUNTRY_NAME";
    @MockBean
    CountryDao dao;
    @Autowired
    CountryService countryService;

    private Country country;
    private Country country2;

    @Test
    public void getCountries() throws Exception {
        givenTwoDifferentCountries();

        when(dao.findAll()).thenReturn(Arrays.asList(country, country2));

        List<Country> result = countryService.getCountries();
        assertEquals(2, result.size());
        assertEquals(CurrencyEnum.EUR, result.get(0).getCurrency());
        assertEquals(CountryEnum.GERMANY, result.get(0).getCountry());
        assertEquals(CurrencyEnum.GBP, result.get(1).getCurrency());
        assertEquals(CountryEnum.UNITED_KINGDOM, result.get(1).getCountry());
        verify(dao, times(1)).findAll();
    }

    @Test
    public void getCountryByNameReturnsOptional() throws Exception {
        when(dao.findByName(COUNTRY_NAME)).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), countryService.getCountryByName(COUNTRY_NAME));
        verify(dao, times(1)).findByName(COUNTRY_NAME);
    }
    @Test
    public void getCountryByName() throws Exception {
        givenSingleCountry();

        when(dao.findByName(COUNTRY_NAME)).thenReturn(Optional.of(country));

        Optional<Country> result = countryService.getCountryByName(COUNTRY_NAME);
        assertEquals(Optional.of(country), result);
        assertEquals(CurrencyEnum.EUR, result.get().getCurrency());
        assertEquals(CountryEnum.GERMANY, result.get().getCountry());
        verify(dao, times(1)).findByName(COUNTRY_NAME);
    }
    private void givenSingleCountry() {
        country = new Country.Builder().currency(CurrencyEnum.EUR).country(CountryEnum.GERMANY).build();
    }
    private void givenTwoDifferentCountries() {
        country = new Country.Builder().currency(CurrencyEnum.EUR).country(CountryEnum.GERMANY).build();
        country2 = new Country.Builder().currency(CurrencyEnum.GBP).country(CountryEnum.UNITED_KINGDOM).build();
    }
}