package dg.projects.salarycalculator.services.Country;

import dg.projects.salarycalculator.models.Country;

import java.util.List;
import java.util.Optional;


public interface CountryService {
    List<Country> getCountries();
    Optional<Country> getCountryByName(String country);
}
