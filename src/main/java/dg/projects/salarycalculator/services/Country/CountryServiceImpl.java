package dg.projects.salarycalculator.services.Country;

import dg.projects.salarycalculator.dao.CountryDao;
import dg.projects.salarycalculator.models.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryDao dao;

    @Autowired
    public CountryServiceImpl(CountryDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Country> getCountries() {
        return dao.findAll();
    }

    public Optional<Country> getCountryByName(String countryName) {
        return dao.findByName(countryName);
    }
}
