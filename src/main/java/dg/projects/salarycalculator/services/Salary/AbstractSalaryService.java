package dg.projects.salarycalculator.services.Salary;

import dg.projects.salarycalculator.dto.CalculateSalaryDTO;

abstract class AbstractSalaryService implements SalaryService {

    Double calculate(CalculateSalaryDTO dto) {
        return exchangeMoney(applyTaxes(calculateMonthlySalary(dto))).getSalary();
    }

    protected abstract CalculateSalaryDTO applyTaxes(CalculateSalaryDTO dto);
    protected abstract CalculateSalaryDTO calculateMonthlySalary(CalculateSalaryDTO dto);
    protected abstract CalculateSalaryDTO exchangeMoney(CalculateSalaryDTO dto);
}
