package com.example.soapclient.controller;

import com.example.soapclient.client.CountryInfoClient;
import com.example.soapclient.client.dto.CountryDetail;
import com.example.soapclient.client.dto.CountrySummary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/countries")
public class CountryInfoController {

    private final CountryInfoClient countryInfoClient;

    public CountryInfoController(CountryInfoClient countryInfoClient) {
        this.countryInfoClient = countryInfoClient;
    }

    @GetMapping
    public List<CountrySummary> countries() {
        return countryInfoClient.countries();
    }

    @GetMapping("/{isoCode}")
    public CountryDetail country(@PathVariable String isoCode) {
        return countryInfoClient.fullCountryInfo(isoCode.toUpperCase(Locale.ROOT));
    }

    @GetMapping("/{isoCode}/capital")
    public Capital capital(@PathVariable String isoCode) {
        String code = isoCode.toUpperCase(Locale.ROOT);
        return new Capital(code, countryInfoClient.capitalCity(code));
    }

    public record Capital(String isoCode, String capitalCity) {}
}
