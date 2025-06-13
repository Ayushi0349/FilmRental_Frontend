package com.filmrentalfrontend.model.dto;

import lombok.Data;

@Data
public class InventoryDTO {
    private Integer inventoryId;
    private Integer filmId;
    private Integer storeId; // Added to complete the class
}