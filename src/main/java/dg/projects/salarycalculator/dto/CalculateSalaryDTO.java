package dg.projects.salarycalculator.dto;

import dg.projects.salarycalculator.models.Country;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
public class CalculateSalaryDTO {
    @NotNull(message = "Valid salary is required")
    private
    Double salary;
    @NotNull(message ="Valid country is required")
    @NotEmpty(message ="Country cannot be empty")
    private
    String countryName;
    private Country country;
}
