package dg.projects.salarycalculator.controllers;

import dg.projects.salarycalculator.dto.CalculateSalaryDTO;
import dg.projects.salarycalculator.services.ExchangeRates.RateNotFoundException;
import dg.projects.salarycalculator.services.Salary.CountryNotFoundException;
import dg.projects.salarycalculator.services.Salary.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/salarycalculator")
class SalaryCalculator {

    private final SalaryService salaryService;

    @Autowired
    public SalaryCalculator(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    @PostMapping
    public Double calculateSalary(@Valid @RequestBody CalculateSalaryDTO dto, HttpServletResponse response) throws IOException {
        try {
            return salaryService.calculateSalary(dto);
        } catch (HttpClientErrorException e) {
            response.sendError(INTERNAL_SERVER_ERROR.value(), e.getStatusText());
        } catch (RateNotFoundException e) {
            response.sendError(INTERNAL_SERVER_ERROR.value(), e.getMessage());
        } catch (CountryNotFoundException e) {
            response.sendError(NOT_IMPLEMENTED.value(), e.getMessage());
        }
        return 0.0;
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleException(MethodArgumentNotValidException ex, HttpServletResponse response) throws IOException {
        BindingResult result = ex.getBindingResult();
        if(result.getFieldErrors().size() == 0) {
            response.sendError(BAD_REQUEST.value(), "Wrong request");
        } else {
            response.sendError(BAD_REQUEST.value(), result.getFieldErrors().stream().findFirst().get().getDefaultMessage());
        }
    }
}
