package dg.projects.salarycalculator.services;

import dg.projects.salarycalculator.dto.NbpRateDTO;
import dg.projects.salarycalculator.dto.RateDTO;
import dg.projects.salarycalculator.enums.CurrencyEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.Optional;

@Service
public class NbpExchangeRates implements ExchangeRates {

    final String remoteUri;
    final String FORMAT = "?format=json";

    public NbpExchangeRates(@Value("${salarycalculator.api.link}") String remoteUri) {
        this.remoteUri = remoteUri;
    }

    @Override
    public Optional<RateDTO> getExchangeRate(CurrencyEnum currency) {
        NbpRateDTO nbpRateDTO = new RestTemplate().getForObject(createRequestUrl(currency, "a"),NbpRateDTO.class);
        return extractNewestRate(nbpRateDTO);
    }

    private String createRequestUrl(CurrencyEnum currencyEnum, String table) {
        return remoteUri+table+"/"+currencyEnum.name()+"/"+FORMAT;
    }

    private Optional<RateDTO> extractNewestRate(NbpRateDTO dto) {
        return dto.getRates().stream().max(Comparator.comparing(RateDTO::getEffectiveDate));
    }
}
