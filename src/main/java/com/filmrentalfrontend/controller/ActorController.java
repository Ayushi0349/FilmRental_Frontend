//package com.filmrentalfrontend.controller;
//
//import com.filmrentalfrontend.model.dto.ActorDTO;
//import com.filmrentalfrontend.model.entity.TeamMember;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Controller
//@RequestMapping("/actors")
//public class ActorController {
//
//    private static final Logger logger = LoggerFactory.getLogger(ActorController.class);
//    private final RestTemplate restTemplate;
//
//    public ActorController(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    @GetMapping("/details/{id}")
//    public String getDetails(
//            @PathVariable Long id,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int size,
//            Model model) {
//        if (!id.equals(1L)) {
//            model.addAttribute("error", "Invalid team member for actor endpoints.");
//            model.addAttribute("endpoints", List.of());
//            return "pagesecond";
//        }
//
//        TeamMember member = new TeamMember(1L, "Alice Smith");
//        model.addAttribute("member", member);
//
//        try {
//            String url = String.format("http://localhost:8080/api/actors/allactorendpoints?page=%d&size=%d", page, size);
//            logger.info("Fetching endpoints from: {}", url);
//            ParameterizedTypeReference<PageResponseDTO<String>> responseType =
//                    new ParameterizedTypeReference<PageResponseDTO<String>>() {};
//            ResponseEntity<PageResponseDTO<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
//            logger.debug("Response status: {}", response.getStatusCode());
//            PageResponseDTO<String> pageResponse = response.getBody();
//            if (pageResponse == null) {
//                logger.warn("Received null response body from: {}", url);
//                model.addAttribute("error", "No data received from API.");
//                model.addAttribute("endpoints", List.of());
//            } else {
//                logger.info("Received {} endpoints for page: {}", pageResponse.getContent().size(), page);
//                model.addAttribute("endpoints", pageResponse.getContent());
//                model.addAttribute("currentPage", page);
//                model.addAttribute("pageSize", size);
//                model.addAttribute("totalEndpoints", pageResponse.getTotalElements());
//            }
//        } catch (HttpClientErrorException e) {
//            logger.error("HTTP error fetching endpoints: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
//            model.addAttribute("error", "Failed to fetch API endpoints: HTTP " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
//            model.addAttribute("endpoints", List.of());
//        } catch (RestClientException e) {
//            logger.error("Error fetching endpoints: {}", e.getMessage(), e);
//            model.addAttribute("error", "Failed to fetch API endpoints: " + e.getMessage());
//            model.addAttribute("endpoints", List.of());
//        }
//
//        return "pagesecond";
//    }
//
//    @GetMapping("/edit/{endpoint}")
//    public String editEndpoint(
//            @PathVariable String endpoint,
//            @RequestParam Long id,
//            @RequestParam int page,
//            @RequestParam int size,
//            Model model) {
//        if (!id.equals(1L)) {
//            model.addAttribute("error", "Invalid team member for actor endpoints.");
//            return "pagethird";
//        }
//
//        TeamMember member = new TeamMember(1L, "Alice Smith");
//        model.addAttribute("member", member);
//        model.addAttribute("endpoint", endpoint);
//        model.addAttribute("page", page);
//        model.addAttribute("size", size);
//
//        try {
//            if (endpoint.equals("GET /api/actors/firstname/{fn}")) {
//                String url = "http://localhost:8080/api/actors/firstname/John";
//                logger.info("Fetching actors from: {}", url);
//                ActorDTO[] actors = restTemplate.getForObject(url, ActorDTO[].class);
//                model.addAttribute("actors", actors != null ? Arrays.asList(actors) : List.of());
//            } else {
//                model.addAttribute("error", "Data fetching not implemented for this endpoint");
//                model.addAttribute("actors", List.of());
//            }
//        } catch (RestClientException e) {
//            logger.error("Error fetching data for endpoint {}: {}", endpoint, e.getMessage(), e);
//            model.addAttribute("error", "Failed to fetch data: " + e.getMessage());
//            model.addAttribute("actors", List.of());
//        }
//
//        return "pagethird";
//    }
//
//    @PostMapping("/update")
//    public String updateEndpoint(
//            @RequestParam Long id,
//            @RequestParam int page,
//            @RequestParam int size,
//            @RequestParam String endpoint,
//            @ModelAttribute("actors") List<ActorDTO> actors,
//            Model model) {
//        if (!id.equals(1L)) {
//            model.addAttribute("error", "Invalid team member for actor endpoints.");
//            return "redirect:/actors/details/" + id + "?page=" + page + "&size=" + size;
//        }
//
//        try {
//            if (endpoint.equals("GET /api/actors/firstname/{fn}")) {
//                for (ActorDTO actor : actors) {
//                    String url = "http://localhost:8080/api/actors/update/firstname/" + actor.getActorId();
//                    logger.info("Updating actor ID {} at: {}", actor.getActorId(), url);
//                    HttpEntity<ActorDTO> request = new HttpEntity<>(actor);
//                    restTemplate.exchange(url, HttpMethod.PUT, request, Void.class);
//                }
//                model.addAttribute("message", "Data updated successfully");
//            } else {
//                model.addAttribute("error", "Update not implemented for this endpoint");
//            }
//        } catch (RestClientException e) {
//            logger.error("Error updating data for endpoint {}: {}", endpoint, e.getMessage(), e);
//            model.addAttribute("error", "Failed to update data: " + e.getMessage());
//        }
//
//        return "redirect:/actors/details/" + id + "?page=" + page + "&size=" + size;
//    }
//}