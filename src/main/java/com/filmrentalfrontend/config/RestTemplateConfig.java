package com.filmrentalfrontend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        converter.setObjectMapper(objectMapper);
        restTemplate.setMessageConverters(Collections.singletonList(converter));

        // Add logging interceptor
        ClientHttpRequestInterceptor loggingInterceptor = (request, body, execution) -> {
            System.out.println("Request URI: " + request.getURI());
            System.out.println("Request Method: " + request.getMethod());
            var response = execution.execute(request, body);
            System.out.println("Response Status: " + response.getStatusCode());
            // Note: Reading response body here may consume it, so be cautious
            return response;
        };
        restTemplate.setInterceptors(List.of(loggingInterceptor));

        return restTemplate;
    }
}