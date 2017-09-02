package dg.projects.salarycalculator.services.Salary;

public class CountryNotFoundException extends RuntimeException{
    public CountryNotFoundException() {
        super("Country was not found in the system");
    }
}
