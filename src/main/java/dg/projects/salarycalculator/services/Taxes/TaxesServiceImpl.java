package dg.projects.salarycalculator.services.Taxes;

import dg.projects.salarycalculator.models.Country;
import org.springframework.stereotype.Service;

@Service
public class TaxesServiceImpl implements TaxesService {

    @Override
    public Double applyTaxesForCountry(Double money, Country country) {
        return reduceTaxes(reduceFixed(money, country), country);
    }

    private Double reduceTaxes(Double salary, Country country) {
        return salary > 0 ? salary-salary*country.getTaxes(): salary;
    }

    private Double reduceFixed(Double salary, Country country) {
        return salary - country.getFixedCost();
    }
}
