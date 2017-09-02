package dg.projects.salarycalculator.services.ExchangeRates;

import dg.projects.salarycalculator.dto.NbpRateDTO;
import dg.projects.salarycalculator.dto.RateDTO;
import dg.projects.salarycalculator.enums.CurrencyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.Optional;

@Service
public class NbpExchangeRates implements ExchangeRates {

    private final String remoteUri;
    private final String FORMAT = "?format=json";
    private final RestTemplate restTemplate;

    @Autowired
    public NbpExchangeRates(RestTemplateBuilder restTemplateBuilder, @Value("${salarycalculator.api.link}") String remoteUri) {
        this.restTemplate = restTemplateBuilder.build();
        this.remoteUri = remoteUri;
    }

    @Override
    public RateDTO getExchangeRate(CurrencyEnum currency) {
        Optional<NbpRateDTO> nbpRateDTO = Optional.ofNullable(restTemplate.getForObject(createRequestUrl(currency),NbpRateDTO.class));
        return extractNewestRate(nbpRateDTO.orElseThrow(RateNotFoundException::new));
    }

    private String createRequestUrl(CurrencyEnum currencyEnum) {
        return remoteUri+ "a/"+currencyEnum.name()+"/"+FORMAT;
    }

    private RateDTO extractNewestRate(NbpRateDTO dto) {
        return dto.getRates().stream().max(Comparator.comparing(RateDTO::getEffectiveDate)).orElseThrow(RateNotFoundException::new);
    }
}
