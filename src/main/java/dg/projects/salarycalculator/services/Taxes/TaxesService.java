package dg.projects.salarycalculator.services.Taxes;

import dg.projects.salarycalculator.models.Country;

public interface TaxesService {
    Double applyTaxesForCountry(Double money, Country country);
}
