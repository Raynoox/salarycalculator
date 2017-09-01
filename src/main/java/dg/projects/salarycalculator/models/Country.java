package dg.projects.salarycalculator.models.countries;

import dg.projects.salarycalculator.enums.CurrencyEnum;
import lombok.Data;

@Data
public class Country {
    CurrencyEnum currency;
    Double taxes;
    Double fixedCost;

    private Country(Builder builder) {
        setCurrency(builder.currency);
        setTaxes(builder.taxes);
        setFixedCost(builder.fixedCost);
    }

    public static final class Builder {
        private CurrencyEnum currency;
        private Double taxes;
        private Double fixedCost;

        public Builder() {
        }

        public Builder currency(CurrencyEnum val) {
            currency = val;
            return this;
        }

        public Builder taxes(Double val) {
            taxes = val;
            return this;
        }

        public Builder fixedCost(Double val) {
            fixedCost = val;
            return this;
        }

        public Country build() {
            return new Country(this);
        }
    }
}
