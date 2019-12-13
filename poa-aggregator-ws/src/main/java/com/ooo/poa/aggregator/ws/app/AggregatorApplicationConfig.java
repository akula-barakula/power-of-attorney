package com.ooo.poa.aggregator.ws.app;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ooo.poa.aggregator.service.AggregatorService;
import com.ooo.poa.aggregator.service.PoaClient;
import com.ooo.poa.aggregator.service.impl.AggregatorServiceImpl;
import com.ooo.poa.aggregator.service.impl.DataAggregator;
import com.ooo.poa.aggregator.service.impl.DataCollector;
import com.ooo.poa.aggregator.service.impl.PoaClientImpl;
import com.ooo.poa.aggregator.ws.controller.AggregatorController;
import com.ooo.poa.aggregator.ws.controller.AggregatorExceptionHandler;
import com.ooo.poa.client.api.AccountApi;
import com.ooo.poa.client.api.CreditCardApi;
import com.ooo.poa.client.api.DebitCardApi;
import com.ooo.poa.client.api.PowerOfAttorneyApi;

@Configuration
@EnableWebMvc
public class AggregatorApplicationConfig {

    @Bean
    public PowerOfAttorneyApi powerOfAttorneyApi() {
        return new PowerOfAttorneyApi();
    }

    @Bean
    public AccountApi accountApi() {
        return new AccountApi();
    }

    @Bean
    public CreditCardApi creditCardApi() {
        return new CreditCardApi();
    }

    @Bean
    public DebitCardApi debitCardApi() {
        return new DebitCardApi();
    }

    @Bean
    public PoaClient poaClient() {
        return new PoaClientImpl();
    }

    @Bean
    public DataCollector dataCollector() {
        return new DataCollector();
    }

    @Bean
    public DataAggregator dataAggregator() {
        return new DataAggregator();
    }

    @Bean
    public AggregatorService aggregatorService() {
        return new AggregatorServiceImpl();
    }

    @Bean
    AggregatorExceptionHandler exceptionHandler() {
    	return new AggregatorExceptionHandler();
    }

    @Bean
    public AggregatorController aggregatorController() {
        return new AggregatorController();
    }


    @Configuration
    public static class AggregatorMvcConfigurer implements WebMvcConfigurer {

        @Override
        public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

            MappingJackson2HttpMessageConverter converter = findJacksonConverter(converters);
            if (converter == null) {
                converter = new MappingJackson2HttpMessageConverter();

                converters.add(0, converter);
            }

            adjustObjectMapper(converter.getObjectMapper());
        }

        private MappingJackson2HttpMessageConverter findJacksonConverter(List<HttpMessageConverter<?>> converters) {

            return (MappingJackson2HttpMessageConverter) converters
                    .stream()
                    .filter(converter -> converter instanceof MappingJackson2HttpMessageConverter)
                    .findFirst()
                    .orElse(null);
        }

        private void adjustObjectMapper(ObjectMapper objectMapper) {

            objectMapper.setSerializationInclusion(Include.NON_EMPTY);
        }
    }
}
