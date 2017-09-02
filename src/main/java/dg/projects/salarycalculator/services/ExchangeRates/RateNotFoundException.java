package dg.projects.salarycalculator.services.ExchangeRates;

public class RateNotFoundException extends RuntimeException {
    public RateNotFoundException() {
        super("Could not find rate for currency");
    }
}
