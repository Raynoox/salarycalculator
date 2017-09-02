package dg.projects.salarycalculator.controllers;

import dg.projects.salarycalculator.SalarycalculatorApplication;
import dg.projects.salarycalculator.enums.CountryEnum;
import dg.projects.salarycalculator.enums.CurrencyEnum;
import dg.projects.salarycalculator.models.Country;
import dg.projects.salarycalculator.services.Country.CountryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = SalarycalculatorApplication.class)
public class CountriesTest {
    @MockBean
    private CountryService countryService;

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    private final CountryEnum POLAND = CountryEnum.POLAND;
    private final CurrencyEnum POLAND_CURRENCY = CurrencyEnum.PLN;
    private final double POLAND_FIXED_COST = 123.0;
    private final double POLAND_TAXES = 321.0;
    private final CountryEnum GERMANY = CountryEnum.GERMANY;
    private final CurrencyEnum GERMANY_CURRENCY = CurrencyEnum.EUR;
    private final double GERMANY_FIXED_COST = 456.0;
    private final double GERMANY_TAXES = 654.0;
    private final String COUNTRIES_END_POINT = "/api/countries";

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
    }
    @Test
    public void getEmptyCountries() throws Exception {
        when(countryService.getCountries()).thenReturn(Collections.emptyList());
        mockMvc.perform(get(COUNTRIES_END_POINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$").value(hasSize(0)));
        verify(countryService, times(1)).getCountries();
    }
    @Test
    public void getCountries() throws Exception {
        Country country1 = new Country.Builder().country(POLAND).currency(POLAND_CURRENCY).fixedCost(POLAND_FIXED_COST).taxes(POLAND_TAXES).build();
        Country country2 = new Country.Builder().country(GERMANY).currency(GERMANY_CURRENCY).fixedCost(GERMANY_FIXED_COST).taxes(GERMANY_TAXES).build();

        when(countryService.getCountries()).thenReturn(Arrays.asList(country1, country2));

        mockMvc.perform(get(COUNTRIES_END_POINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$").value(hasSize(2)))
                .andExpect(jsonPath("$[0].country").value(POLAND.name()))
                .andExpect(jsonPath("$[0].currency").value(POLAND_CURRENCY.name()))
                .andExpect(jsonPath("$[0].taxes").value(POLAND_TAXES))
                .andExpect(jsonPath("$[0].fixedCost").value(POLAND_FIXED_COST))
                .andExpect(jsonPath("$[1].country").value(GERMANY.name()))
                .andExpect(jsonPath("$[1].currency").value(GERMANY_CURRENCY.name()))
                .andExpect(jsonPath("$[1].taxes").value(GERMANY_TAXES))
                .andExpect(jsonPath("$[1].fixedCost").value(GERMANY_FIXED_COST)) ;
        verify(countryService, times(1)).getCountries();
    }
}