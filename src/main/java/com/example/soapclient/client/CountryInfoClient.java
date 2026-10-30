package com.example.soapclient.client;

import com.example.sample.countryinfo.wsdl.CapitalCity;
import com.example.sample.countryinfo.wsdl.CapitalCityResponse;
import com.example.sample.countryinfo.wsdl.FullCountryInfo;
import com.example.sample.countryinfo.wsdl.FullCountryInfoResponse;
import com.example.sample.countryinfo.wsdl.ListOfCountryNamesByName;
import com.example.sample.countryinfo.wsdl.ListOfCountryNamesByNameResponse;
import com.example.sample.countryinfo.wsdl.TCountryInfo;
import com.example.soapclient.client.dto.CountryDetail;
import com.example.soapclient.client.dto.CountrySummary;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import java.util.List;
import java.util.Objects;

/**
 * CountryInfoService 호출. WSDL의 soapAction이 모두 ""이므로 SoapActionCallback은 쓰지 않는다.
 */
@Component
public class CountryInfoClient {

    private final WebServiceTemplate countryInfoTemplate;

    public CountryInfoClient(@Qualifier("countryInfoTemplate") WebServiceTemplate countryInfoTemplate) {
        this.countryInfoTemplate = countryInfoTemplate;
    }

    public String capitalCity(String countryIsoCode) {
        CapitalCity request = new CapitalCity();
        request.setSCountryISOCode(countryIsoCode);

        CapitalCityResponse response = (CapitalCityResponse) countryInfoTemplate.marshalSendAndReceive(request);
        return Objects.requireNonNull(response).getCapitalCityResult();
    }

    public CountryDetail fullCountryInfo(String countryIsoCode) {
        FullCountryInfo request = new FullCountryInfo();
        request.setSCountryISOCode(countryIsoCode);

        FullCountryInfoResponse response = (FullCountryInfoResponse) countryInfoTemplate.marshalSendAndReceive(request);
        TCountryInfo info = Objects.requireNonNull(response).getFullCountryInfoResult();

        List<CountryDetail.Language> languages = info.getLanguages() == null
                ? List.of()
                : info.getLanguages().getTLanguage().stream()
                        .map(l -> new CountryDetail.Language(l.getSISOCode(), l.getSName()))
                        .toList();

        return new CountryDetail(
                info.getSISOCode(),
                info.getSName(),
                info.getSCapitalCity(),
                info.getSPhoneCode(),
                info.getSContinentCode(),
                info.getSCurrencyISOCode(),
                info.getSCountryFlag(),
                languages);
    }

    public List<CountrySummary> countries() {
        ListOfCountryNamesByNameResponse response = (ListOfCountryNamesByNameResponse) countryInfoTemplate
                .marshalSendAndReceive(new ListOfCountryNamesByName());

        return Objects.requireNonNull(response).getListOfCountryNamesByNameResult().getTCountryCodeAndName().stream()
                .map(c -> new CountrySummary(c.getSISOCode(), c.getSName()))
                .toList();
    }
}
