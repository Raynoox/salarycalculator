# Salary calculator

## HOW-TO RUN
* to run tests "./gradlew test"
* to run app on port 8080 "./gradlew bootRun"

## TO ADD NEW CURRENCY
* in application.properties add attribute to salarycalculator.country as JSON object with attributes:
 * "CURRENCY" -> currency code of country (must be added to CurrencyEnum.java too)
 * "FIXED_COST" -> float of fixed costs in country
 * "TAXES" -> float of percentage that apply in country
 ### example
    COUNTRY_NAME: {
        "CURRENCY": EUR,
        "FIXED_COST": 1000.0,
        "TAXES": 0.19
    }
 ### IMPORTANT
     * COUNTRY_NAME must be declared in CountryEnum.java

     * CURRENCY must be declared in CurrencyEnum.java

     * for available currencies in current implementation see http://api.nbp.pl/api/exchangerates/tables/a/?format=json (only table A supported!)