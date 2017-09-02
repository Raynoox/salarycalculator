package dg.projects.salarycalculator.controllers;

import dg.projects.salarycalculator.models.Country;
import dg.projects.salarycalculator.services.Country.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
class Countries {

    private final CountryService countryService;

    @Autowired
    public Countries(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public List<Country> getCountries() {
        return countryService.getCountries();
    }


}
