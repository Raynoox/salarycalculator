package dg.projects.salarycalculator.dao;

import dg.projects.salarycalculator.enums.CountryEnum;
import dg.projects.salarycalculator.enums.CurrencyEnum;
import dg.projects.salarycalculator.models.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CountryDaoImpl implements CountryDao{
    private final Map<CountryEnum, Country> countryMap = new HashMap<>();


    @Autowired
    public CountryDaoImpl(@Value("#{${salarycalculator.country}}") Map<String, Map<String, Object>> map) {
        createMap(map);
    }
    private void createMap(Map<String, Map<String, Object>> map) {
        map.keySet().forEach(
                key -> createMapEntry(key, map.get(key))
        );
    }
    private void createMapEntry(String key, Map<String, Object> countryData) {
        countryMap.put(CountryEnum.valueOf(key),
                new Country.Builder()
                        .currency(CurrencyEnum.valueOf((String)countryData.get("CURRENCY")))
                        .fixedCost((Double) countryData.get("FIXED_COST"))
                        .taxes((Double) countryData.get("TAXES"))
                        .country(CountryEnum.valueOf(key))
                        .build());
    }
    @Override
    public List<Country> findAll() {
        return countryMap.entrySet().stream()
                .map(Map.Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public Optional<Country> findByName(String countryName) {
        return Optional.ofNullable(countryMap.get(CountryEnum.valueOf(countryName)));

    }
}
