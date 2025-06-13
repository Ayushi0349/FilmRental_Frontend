package com.filmrentalfrontend.controller;

import com.filmrentalfrontend.model.dto.CustomerDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerWebController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_BASE_URL = "http://localhost:8080/api/customers";

    @GetMapping
    public String listCustomers(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                Model model) {
        String url = API_BASE_URL + "/all?page=" + page + "&size=" + size;
        ResponseEntity<PaginatedResponse> response = restTemplate.getForEntity(url, PaginatedResponse.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            model.addAttribute("customers", response.getBody().getContent());
            model.addAttribute("totalPages", response.getBody().getTotalPages());
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", size);
            model.addAttribute("pageSizes", new int[]{5, 10, 15, 20});
        } else {
            model.addAttribute("error", "Could not fetch customer data");
        }

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
            model.addAttribute("page", page);
            model.addAttribute("size", size);
            model.addAttribute("pageSizes", new int[]{5, 10, 15, 20});
            return "customer-form";

        } catch (Exception e) {
            model.addAttribute("error", "Failed to fetch customer data: " + e.getMessage());
            return "customer-form";
        }
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
            model.addAttribute("error", "Failed to update customer: " + e.getMessage());
            model.addAttribute("customer", customerDTO);
            model.addAttribute("page", page);
            model.addAttribute("size", size);
            model.addAttribute("pageSizes", new int[]{5, 10, 15, 20});
            return "customer-form";
        }
    }

    // Inner static class for paginated response
    public static class PaginatedResponse {
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






//package com.filmrentalfrontend.controller;
//
//import com.filmrentalfrontend.model.dto.CustomerDTO;
//import org.springframework.http.*;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/customers")
//public class CustomerWebController {
//
//    private final RestTemplate restTemplate = new RestTemplate();
//    private final String API_BASE_URL = "http://localhost:8080/api/customers";
//
//    @GetMapping
//    public String listCustomers(@RequestParam(defaultValue = "0") int page,
//                                @RequestParam(defaultValue = "10") int size,
//                                Model model) {
//        String url = API_BASE_URL + "/all?page=" + page + "&size=" + size;
//        ResponseEntity<PaginatedResponse> response = restTemplate.getForEntity(url, PaginatedResponse.class);
//
//        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//            model.addAttribute("customers", response.getBody().getContent());
//            model.addAttribute("totalPages", response.getBody().getTotalPages());
//            model.addAttribute("currentPage", page);
//            model.addAttribute("pageSize", size);
//            model.addAttribute("pageSizes", new int[]{5, 10, 15, 20});
//        } else {
//            model.addAttribute("error", "Could not fetch customer data");
//        }
//
//        return "customer-list";
//    }
//
//    @GetMapping("/edit/{id}")
//    public String editCustomer(@PathVariable Integer id,
//                               @RequestParam int page,
//                               @RequestParam int size,
//                               Model model) {
//        try {
//            String url = API_BASE_URL + "/" + id;
//            CustomerDTO customer = restTemplate.getForObject(url, CustomerDTO.class);
//
//            if (customer == null) {
//                model.addAttribute("error", "Customer not found with ID: " + id);
//                return "customer-form";
//            }
//
//            model.addAttribute("customer", customer);
//            model.addAttribute("page", page);
//            model.addAttribute("size", size);
//            model.addAttribute("pageSizes", new int[]{5, 10, 15, 20});
//            return "customer-form";
//
//        } catch (Exception e) {
//            model.addAttribute("error", "Failed to fetch customer data: " + e.getMessage());
//            return "customer-form";
//        }
//    }
//
//    @PostMapping("/update/{id}")
//    public String updateCustomer(@PathVariable Integer id,
//                                 @ModelAttribute("customer") CustomerDTO customerDTO,
//                                 @RequestParam int page,
//                                 @RequestParam int size,
//                                 Model model) {
//        try {
//            customerDTO.setCustomerId(id);
//            String url = API_BASE_URL + "/update/" + id;
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            HttpEntity<CustomerDTO> requestEntity = new HttpEntity<>(customerDTO, headers);
//
//            restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
//
//            return "redirect:/customers?page=" + page + "&size=" + size;
//
//        } catch (Exception e) {
//            model.addAttribute("error", "Failed to update customer: " + e.getMessage());
//            model.addAttribute("customer", customerDTO);
//            model.addAttribute("page", page);
//            model.addAttribute("size", size);
//            model.addAttribute("pageSizes", new int[]{5, 10, 15, 20});
//            return "customer-form";
//        }
//    }

//    public static class PaginatedResponse {
//        private List<CustomerDTO> content;
//        private int totalPages;
//
//        public List<CustomerDTO> getContent() {
//            return content;
//        }
//
//        public void setContent(List<CustomerDTO> content) {
//            this.content = content;
//        }
//
//        public int getTotalPages() {
//            return totalPages;
//        }
//
//        public void setTotalPages(int totalPages) {
//            this.totalPages = totalPages;
//        }
//    }

//    // Inner static class for paginated response
//    public static class PaginatedResponse {
//        private List<CustomerDTO> content;
//        private int totalPages;
//
//        public List<CustomerDTO> getContent() {
//            return content;
//        }
//
//        public void setContent(List<CustomerDTO> content) {
//            this.content = content;
//        }
//
//        public int getTotalPages() {
//            return totalPages;
//        }
//
//        public void setTotalPages(int totalPages) {
//            this.totalPages = totalPages;
//        }
//    }
