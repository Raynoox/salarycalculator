package dg.projects.salarycalculator.controllers;

import dg.projects.salarycalculator.SalarycalculatorApplication;
import dg.projects.salarycalculator.dto.CalculateSalaryDTO;
import dg.projects.salarycalculator.services.ExchangeRates.RateNotFoundException;
import dg.projects.salarycalculator.services.Salary.CountryNotFoundException;
import dg.projects.salarycalculator.services.Salary.SalaryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = SalarycalculatorApplication.class)
public class SalaryCalculatorTest {
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @MockBean
    private SalaryService salaryService;

    private final String END_POINT = "/api/salarycalculator";
    private final String GERMANY_NAME = "GERMANY";
    private final Double GERMANY_SALARY = 1234.0;
    private final Double GERMANY_RESULT = 123400.0;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testCalculateSalary() throws Exception {
        CalculateSalaryDTO dto = createDTO(GERMANY_NAME, GERMANY_SALARY);
        when(salaryService.calculateSalary(dto)).thenReturn(GERMANY_RESULT);
        performPostAction(dto)
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$").value(GERMANY_RESULT));
        verify(salaryService, times(1)).calculateSalary(dto);
    }
    @Test
    public void returnsNotImplementedWhenNoCountryFound() throws Exception {
        CalculateSalaryDTO dto = createDTO(GERMANY_NAME, GERMANY_SALARY);
        doThrow(new CountryNotFoundException()).when(salaryService).calculateSalary(dto);
        performPostAction(dto)
                .andExpect(status().isNotImplemented());
        verify(salaryService, times(1)).calculateSalary(dto);
    }
    @Test
    public void returnsInternalWhenCouldNotFindRate() throws Exception {
        CalculateSalaryDTO dto = createDTO(GERMANY_NAME, GERMANY_SALARY);
        doThrow(new RateNotFoundException()).when(salaryService).calculateSalary(dto);
        performPostAction(dto)
                .andExpect(status().isInternalServerError());
        verify(salaryService, times(1)).calculateSalary(dto);
    }
    @Test
    public void returnsInternalWhenInternalHttpError() throws Exception {
        CalculateSalaryDTO dto = createDTO(GERMANY_NAME, GERMANY_SALARY);
        doThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND)).when(salaryService).calculateSalary(dto);
        performPostAction(dto)
                .andExpect(status().isInternalServerError());
        verify(salaryService, times(1)).calculateSalary(dto);
    }
    @Test
    public void testValidSalaryDto() throws Exception {
        CalculateSalaryDTO dto = createDTO(GERMANY_NAME, null);
        when(salaryService.calculateSalary(dto)).thenReturn(GERMANY_RESULT);
        performPostAction(dto)
                .andExpect(status().isBadRequest());
        verify(salaryService, times(0)).calculateSalary(dto);
    }
    @Test
    public void testValidCountryName() throws Exception {
        CalculateSalaryDTO dto = createDTO("", GERMANY_SALARY);
        when(salaryService.calculateSalary(dto)).thenReturn(GERMANY_RESULT);
        performPostAction(dto)
                .andExpect(status().isBadRequest());
        verify(salaryService, times(0)).calculateSalary(dto);
    }
    private CalculateSalaryDTO createDTO(String countryName, Double salary) {
        CalculateSalaryDTO result = new CalculateSalaryDTO();
        result.setSalary(salary);
        result.setCountryName(countryName);
        return result;
    }
    private ResultActions performPostAction(CalculateSalaryDTO dto) throws Exception {
        return mockMvc.perform(post(END_POINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"countryName\":\""+dto.getCountryName()+"\",\"salary\":\""+dto.getSalary()+"\"}"));
    }
}