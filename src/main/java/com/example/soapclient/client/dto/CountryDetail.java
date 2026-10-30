package com.example.soapclient.client.dto;

import java.util.List;

public record CountryDetail(String isoCode,
                            String name,
                            String capitalCity,
                            String phoneCode,
                            String continentCode,
                            String currencyIsoCode,
                            String flagUrl,
                            List<Language> languages) {

    public record Language(String isoCode, String name) {}
}
