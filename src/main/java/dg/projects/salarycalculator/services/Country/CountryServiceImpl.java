package dg.projects.salarycalculator.services;

import dg.projects.salarycalculator.dao.CountryDao;
import dg.projects.salarycalculator.models.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Scope("singleton")
public class CountryServiceImpl implements CountryService {

    private final CountryDao dao;

    @Autowired
    public CountryServiceImpl(CountryDao dao) {
        this.dao = dao;
    }

    private static <K, U> Collector<AbstractMap.SimpleEntry<K, U>, ?, Map<K, U>> entriesToMap() {
        return Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue);
    }
    @Override
    public List<Country> getCountries() {
        return dao.findAll();
    }
}
