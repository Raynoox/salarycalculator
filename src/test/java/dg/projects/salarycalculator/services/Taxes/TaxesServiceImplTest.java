package dg.projects.salarycalculator.services.Taxes;

import dg.projects.salarycalculator.enums.CountryEnum;
import dg.projects.salarycalculator.enums.CurrencyEnum;
import dg.projects.salarycalculator.models.Country;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TaxesServiceImpl.class)
public class TaxesServiceImplTest {

    @Autowired
    private TaxesService taxesService;
    private final Double FIXED_COST = 1000.0;
    private final Double TAXES = 0.2;
    @Test
    public void doesNotApplyTaxesIfSalaryNotPositive() throws Exception {
        Country country = createGenericCountry();
        Double salary = 1000.0;
        Double result = taxesService.applyTaxesForCountry(salary, country);
        Double expected = expectedWithoutTaxes(salary, FIXED_COST);
        assertEquals(expected, result);
    }
    @Test
    public void applyTaxesIfSalaryPositive() throws Exception {
        Country country = createGenericCountry();
        Double salary = 2000.0;
        Double result = taxesService.applyTaxesForCountry(salary, country);
        Double expected = expectedWithTaxes(salary, FIXED_COST, TAXES);
        assertEquals(expected, result);
    }
    private Double expectedWithoutTaxes(Double money, Double fixed_cost) {
        return money-fixed_cost;
    }
    private Double expectedWithTaxes(Double money, Double fixed_cost, Double taxes) {
        return expectedWithoutTaxes(money, fixed_cost)*(1-taxes);
    }
    private Country createGenericCountry() {
        return new Country.Builder().country(CountryEnum.GERMANY).currency(CurrencyEnum.EUR).fixedCost(FIXED_COST).taxes(TAXES).build();
    }

}