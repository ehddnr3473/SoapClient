package com.example.soapclient.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.http.client.HttpClientSettings;
import org.springframework.boot.webservices.client.WebServiceMessageSenderFactory;
import org.springframework.boot.webservices.client.WebServiceTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SoapClientProperties.class)
public class SoapClientConfig {

    @Bean
    public Jaxb2Marshaller calculatorMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.example.sample.calculator.wsdl");
        return marshaller;
    }

    @Bean
    public Jaxb2Marshaller countryInfoMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.example.sample.countryinfo.wsdl");
        return marshaller;
    }

    @Bean
    public WebServiceTemplate calculatorTemplate(WebServiceTemplateBuilder builder,
                                                 @Qualifier("calculatorMarshaller") Jaxb2Marshaller marshaller,
                                                 SoapClientProperties props) {
        return build(builder, marshaller, props.calculator().url(), props);
    }

    @Bean
    public WebServiceTemplate countryInfoTemplate(WebServiceTemplateBuilder builder,
                                                  @Qualifier("countryInfoMarshaller") Jaxb2Marshaller marshaller,
                                                  SoapClientProperties props) {
        return build(builder, marshaller, props.countryInfo().url(), props);
    }

    private WebServiceTemplate build(WebServiceTemplateBuilder builder,
                                     Jaxb2Marshaller marshaller,
                                     String url,
                                     SoapClientProperties props) {
        HttpClientSettings settings = HttpClientSettings.defaults()
                .withConnectTimeout(props.connectTimeout())
                .withReadTimeout(props.readTimeout());
        return builder
                .httpMessageSenderFactory(WebServiceMessageSenderFactory.http(settings))
                .setDefaultUri(url)
                .setMarshaller(marshaller)
                .setUnmarshaller(marshaller)
                .additionalInterceptors(new UserAgentInterceptor("soapclient/1.0"))
                .build();
    }
}
