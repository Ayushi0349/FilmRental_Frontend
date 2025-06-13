package com.filmrentalfrontend.model.dto;

import lombok.Data;

@Data
public class TopFilmDTO {
    private Integer filmId;
    private String title;
    private Long rentalCount;
}