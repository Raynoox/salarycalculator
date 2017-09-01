package dg.projects.salarycalculator.services.Salary;

public class RateNotFoundException extends RuntimeException {
    RateNotFoundException() {
        super("Could not find rate for currency");
    }
}
