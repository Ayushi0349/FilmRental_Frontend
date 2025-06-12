package com.filmrentalfrontend.model.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CustomerDTO {
    private Integer customerId;
    private String firstName;
    private String lastName;
    private String email;
    private Integer storeId;
    private Integer addressId;
    private String phone;
    private Boolean active;
    private LocalDate createDate;
    private LocalDateTime lastUpdate;
}