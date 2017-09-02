package dg.projects.salarycalculator.services.ExchangeRates;

import dg.projects.salarycalculator.dto.NbpRateDTO;
import dg.projects.salarycalculator.dto.RateDTO;
import dg.projects.salarycalculator.enums.CurrencyEnum;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NbpExchangeRates.class)
public class NbpExchangeRatesTest {

    @MockBean
    RestTemplateBuilder restTemplateBuilder;
    @Mock
    private
    RestTemplate restTemplate = mock(RestTemplate.class);
    @Autowired
    ExchangeRates nbpExchangeRates;

    private final String exampleUri="example_uri";

    private RateDTO dto1;
    private RateDTO dto2;
    @Before
    public void setUp() throws Exception {
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        nbpExchangeRates = new NbpExchangeRates(restTemplateBuilder, exampleUri);
    }
    @Test(expected = HttpClientErrorException.class)
    public void throwsExceptionIfUriWrong() {
        givenRestTemplateThrowsNotFound();
        nbpExchangeRates.getExchangeRate(CurrencyEnum.EUR);
    }
    @Test(expected = RateNotFoundException.class)
    public void throwsExceptionIfHttpReturnedNull() {
        givenRestTemplateReturnsNull();
        nbpExchangeRates.getExchangeRate(CurrencyEnum.EUR);
    }
    @Test(expected = RateNotFoundException.class)
    public void throwsExceptionIfEmptyList() {
        givenRestTemplateReturnsRateWithEmptyResult();
        nbpExchangeRates.getExchangeRate(CurrencyEnum.EUR);
    }
    @Test
    public void returnsNewestIfMultiple() {
        givenRestTemplateReturnsMultipleRates();
        RateDTO result = nbpExchangeRates.getExchangeRate(CurrencyEnum.EUR);
        assertEquals(dto2, result);
    }


    private void givenRestTemplateThrowsNotFound() {
        doThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND)).when(restTemplate).getForObject(Mockito.anyString(), any());
    }
    private void givenRestTemplateReturnsNull() {
        when(restTemplate.getForObject(Mockito.anyString(), any())).thenReturn(null);
    }
    private void givenRestTemplateReturnsRateWithEmptyResult() {
        NbpRateDTO result = new NbpRateDTO();
        result.setRates(Collections.emptyList());
        when(restTemplate.getForObject(Mockito.anyString(), any())).thenReturn(result);
    }
    private void givenRestTemplateReturnsMultipleRates() {

        dto1 = new RateDTO();
        dto1.setEffectiveDate(Date.from(Instant.ofEpochSecond(1000000)));
        dto1.setNo("1");

        dto2 = new RateDTO();
        dto2.setEffectiveDate(Date.from(Instant.ofEpochSecond(2000000)));
        dto1.setNo("2");

        NbpRateDTO result = new NbpRateDTO();
        result.setRates(Arrays.asList(dto1, dto2));
        when(restTemplate.getForObject(Mockito.anyString(), any())).thenReturn(result);
    }

}