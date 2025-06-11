package com.filmrentalfrontend.controller;

import com.filmrentalfrontend.model.FilmDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
public class FilmController {
    @Autowired
    private RestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:8080/api/films";

    @GetMapping("/language/{lang}")
    public String language(@PathVariable String lang, Model model) {
        String url = BASE_URL + "/language/" + lang;
        ResponseEntity<FilmDTO> response = restTemplate.getForEntity(url, FilmDTO.class);
        List<FilmDTO> films = Arrays.asList(response.getBody());
        model.addAttribute("films", films);
        return "list";
    }
}