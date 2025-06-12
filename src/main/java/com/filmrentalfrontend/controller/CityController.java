package com.filmrentalfrontend.controller;

import com.filmrentalfrontend.model.dto.CityDTO;
import com.filmrentalfrontend.model.dto.PageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("/cities")
public class CityController {

    private static final Logger logger = LoggerFactory.getLogger(CityController.class);
    private final RestTemplate restTemplate;

    public CityController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/list")
    public String getAllCities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        try {
            String url = String.format("http://localhost:8080/api/cities?page=%d&size=%d", page, size);
            logger.info("Fetching cities from: {}", url);

            ParameterizedTypeReference<PageDTO<CityDTO>> responseType =
                    new ParameterizedTypeReference<PageDTO<CityDTO>>() {};
            ResponseEntity<PageDTO<CityDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);

            logger.debug("Response status: {}", response.getStatusCode());
            PageDTO<CityDTO> pageResponse = response.getBody();

            if (pageResponse == null) {
                logger.warn("Received null response body from: {}", url);
                model.addAttribute("error", "No data received from API.");
                model.addAttribute("cities", new PageDTO<CityDTO>());
            } else {
                logger.info("Received {} cities for page: {}", pageResponse.getContent().size(), page);
                model.addAttribute("cities", pageResponse);
                model.addAttribute("currentPage", page);
                model.addAttribute("pageSize", size);
            }
        } catch (HttpClientErrorException e) {
            logger.error("HTTP error fetching cities: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            model.addAttribute("error", "Failed to fetch cities: HTTP " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            model.addAttribute("cities", new PageDTO<CityDTO>());
        } catch (RestClientException e) {
            logger.error("Error fetching cities: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to fetch cities: " + e.getMessage());
            model.addAttribute("cities", new PageDTO<CityDTO>());
        }

        return "city";
    }
}