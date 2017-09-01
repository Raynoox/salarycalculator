package dg.projects.salarycalculator.services.Salary;


import dg.projects.salarycalculator.dto.CalculateSalaryDTO;

public interface SalaryService {
    Double calculateSalary(CalculateSalaryDTO dto);
}
