package dg.projects.salarycalculator.dao;


import dg.projects.salarycalculator.models.Country;

import java.util.List;
import java.util.Optional;

public interface CountryDao {
    List<Country> findAll();
    Optional<Country> findByName(String countryName);
}
