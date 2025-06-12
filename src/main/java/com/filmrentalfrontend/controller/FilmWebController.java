package com.filmrentalfrontend.controller;

import com.filmrentalfrontend.model.dto.ActorDTO;
import com.filmrentalfrontend.model.dto.FilmDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties.UiService.LOGGER;

@Controller
public class FilmWebController {

    private final RestTemplate restTemplate;

    @Value("${backend.api.url:http://localhost:8080/api/films}")
    private String backendApiUrl;

    public FilmWebController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @GetMapping("/films")
    public String listFilms(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        try {
            String url = backendApiUrl + "/all?page=" + page + "&size=" + size;
            ResponseEntity<PageResponse<FilmDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<PageResponse<FilmDTO>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                model.addAttribute("films", response.getBody().getContent());
                model.addAttribute("currentPage", page);
                model.addAttribute("totalPages", response.getBody().getTotalPages());
            } else {
                model.addAttribute("films", null);
                model.addAttribute("error", "No films found or error occurred");
            }
        } catch (Exception e) {
            model.addAttribute("films", null);
            model.addAttribute("error", "Error fetching films: " + e.getMessage());
        }
        return "film-list";
    }

    @GetMapping("/films/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        try {
            // Since your API doesn't have actor-list.html get-by-ID endpoint, fetch all films and filter
            String url = backendApiUrl + "/all?page=0&size=" + Integer.MAX_VALUE;
            ResponseEntity<PageResponse<FilmDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<PageResponse<FilmDTO>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                FilmDTO film = response.getBody().getContent().stream()
                        .filter(f -> f.getFilmId().equals(id))
                        .findFirst()
                        .orElse(null);
                if (film != null) {
                    model.addAttribute("film", film);
                    return "film-edit";
                }
            }
            return "redirect:/films?error=Film not found";
        } catch (Exception e) {
            return "redirect:/films?error=Error fetching film: " + e.getMessage();
        }
    }

    @PostMapping("/films/update/{id}")
    public String updateFilm(@PathVariable Integer id, @ModelAttribute FilmDTO filmDTO) {
        try {
            // Update title
            String titleUrl = backendApiUrl + "/update/title/" + id;
            ResponseEntity<FilmDTO> titleResponse = restTemplate.exchange(
                    titleUrl,
                    HttpMethod.PUT,
                    new HttpEntity<>(filmDTO.getTitle()),
                    FilmDTO.class
            );

            // Update release year
            String yearUrl = backendApiUrl + "/update/releaseyear/" + id;
            ResponseEntity<FilmDTO> yearResponse = restTemplate.exchange(
                    yearUrl,
                    HttpMethod.PUT,
                    new HttpEntity<>(filmDTO.getReleaseYear().getValue()),
                    FilmDTO.class
            );

            // Update rental duration
            String durationUrl = backendApiUrl + "/update/rentalduration/" + id;
            ResponseEntity<FilmDTO> durationResponse = restTemplate.exchange(
                    durationUrl,
                    HttpMethod.PUT,
                    new HttpEntity<>(filmDTO.getRentalDuration()),
                    FilmDTO.class
            );

            // Update rental rate
            String rateUrl = backendApiUrl + "/update/rentalrate/" + id;
            ResponseEntity<FilmDTO> rateResponse = restTemplate.exchange(
                    rateUrl,
                    HttpMethod.PUT,
                    new HttpEntity<>(filmDTO.getRentalRate()),
                    FilmDTO.class
            );

            if (titleResponse.getStatusCode() == HttpStatus.OK &&
                    yearResponse.getStatusCode() == HttpStatus.OK &&
                    durationResponse.getStatusCode() == HttpStatus.OK &&
                    rateResponse.getStatusCode() == HttpStatus.OK) {
                return "redirect:/films?success=Updated successfully";
            }
            return "redirect:/films?error=Update failed";
        } catch (Exception e) {
            return "redirect:/films?error=Update failed: " + e.getMessage();
        }
    }



    @GetMapping("/films/add")
    public String showAddForm(Model model) {
        FilmDTO filmDTO = new FilmDTO();
        model.addAttribute("film", filmDTO);

        try {
            String actorsUrl = backendApiUrl.replace("/films", "/actors/all");
            ResponseEntity<List<ActorDTO>> actorsResponse = restTemplate.exchange(
                    actorsUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ActorDTO>>() {}
            );

            if (actorsResponse.getStatusCode() == HttpStatus.OK && actorsResponse.getBody() != null) {
                model.addAttribute("actors", actorsResponse.getBody());
            } else {
                model.addAttribute("actors", null);
                model.addAttribute("warning", "Unable to load actors. Proceed without selecting actors.");
            }
        } catch (Exception e) {
            model.addAttribute("actors", null);
            model.addAttribute("warning", "Unable to load actors. Proceed without selecting actors.");
        }
        return "film-add";
    }

    @PostMapping("/films/add")
    public String addFilm(@ModelAttribute FilmDTO filmDTO) {
        try {
            String url = backendApiUrl;
            ResponseEntity<String> response = restTemplate.postForEntity(
                    url,
                    filmDTO,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.CREATED) {
                return "redirect:/films?success=Film added successfully";
            }
            return "redirect:/films?error=Add failed";
        } catch (Exception e) {
            return "redirect:/films?error=Add failed";
        }
    }


    // Helper class to handle paginated response
    public static class PageResponse<T> {
        private List<T> content;
        private int totalPages;

        public List<T> getContent() {
            return content;
        }

        public void setContent(List<T> content) {
            this.content = content;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }
    }
}