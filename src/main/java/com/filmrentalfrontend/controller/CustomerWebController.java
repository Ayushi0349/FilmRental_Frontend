package com.filmrentalfrontend.controller;

import com.filmrentalfrontend.model.dto.CustomerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerWebController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerWebController.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_BASE_URL = "http://localhost:8080/api/customers";
    private static final int[] PAGE_SIZES = {5, 10, 15, 20};

    @GetMapping
    public String listCustomers(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                Model model) {
        String url = API_BASE_URL + "/all?page=" + page + "&size=" + size;

        try {
            ResponseEntity<PaginatedCustomerResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                model.addAttribute("customers", response.getBody().getContent());
                model.addAttribute("totalPages", response.getBody().getTotalPages());
            } else {
                logger.warn("Unexpected response status: {}", response.getStatusCode());
                model.addAttribute("customers", Collections.emptyList());
                model.addAttribute("error", "Unable to fetch customers.");
            }
        } catch (Exception e) {
            logger.error("Error fetching customer list", e);
            model.addAttribute("customers", Collections.emptyList());
            model.addAttribute("error", "Failed to retrieve customer data.");
        }

        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("pageSizes", PAGE_SIZES);
        return "customer-list";
    }

    @GetMapping("/edit-customer")
    public String editCustomer(@RequestParam Integer id,
                               @RequestParam int page,
                               @RequestParam int size,
                               Model model) {
        try {
            String url = API_BASE_URL + "/" + id;
            CustomerDTO customer = restTemplate.getForObject(url, CustomerDTO.class);

            if (customer == null) {
                model.addAttribute("error", "Customer not found with ID: " + id);
                return "customer-form";
            }

            model.addAttribute("customer", customer);
        } catch (Exception e) {
            logger.error("Error fetching customer with ID: {}", id, e);
            model.addAttribute("error", "Failed to fetch customer data.");
        }

        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("pageSizes", PAGE_SIZES);
        return "customer-form";
    }

    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable Integer id,
                                 @ModelAttribute("customer") CustomerDTO customerDTO,
                                 @RequestParam int page,
                                 @RequestParam int size,
                                 Model model) {
        try {
            customerDTO.setCustomerId(id);
            String url = API_BASE_URL + "/update/" + id;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CustomerDTO> requestEntity = new HttpEntity<>(customerDTO, headers);

            restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);

            return "redirect:/customers?page=" + page + "&size=" + size;
        } catch (Exception e) {
            logger.error("Failed to update customer with ID: {}", id, e);
            model.addAttribute("error", "Failed to update customer: " + e.getMessage());
            model.addAttribute("customer", customerDTO);
            model.addAttribute("page", page);
            model.addAttribute("size", size);
            model.addAttribute("pageSizes", PAGE_SIZES);
            return "customer-form";
        }
    }

    // Strongly typed response class for pagination
    public static class PaginatedCustomerResponse {
        private List<CustomerDTO> content;
        private int totalPages;

        public List<CustomerDTO> getContent() {
            return content;
        }

        public void setContent(List<CustomerDTO> content) {
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
