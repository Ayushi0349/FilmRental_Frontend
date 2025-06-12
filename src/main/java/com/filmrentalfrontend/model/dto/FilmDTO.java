package com.filmrentalfrontend.model.dto;

import java.math.BigDecimal;
import java.util.List;

public class FilmDTO {
    private Integer filmId;
    private String title;
    private String description;
    private Integer releaseYear;
    private Integer languageId;
    private Integer originalLanguageId;
    private Integer rentalDuration;
    private BigDecimal rentalRate;
    private Integer length;
    private BigDecimal replacementCost;
    private String rating;
    private String specialFeatures;
    private List<Integer> actorIds;

    // Getters and Setters
    public Integer getFilmId() { return filmId; }
    public void setFilmId(Integer filmId) { this.filmId = filmId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }
    public Integer getLanguageId() { return languageId; }
    public void setLanguageId(Integer languageId) { this.languageId = languageId; }
    public Integer getOriginalLanguageId() { return originalLanguageId; }
    public void setOriginalLanguageId(Integer originalLanguageId) { this.originalLanguageId = originalLanguageId; }
    public Integer getRentalDuration() { return rentalDuration; }
    public void setRentalDuration(Integer rentalDuration) { this.rentalDuration = rentalDuration; }
    public BigDecimal getRentalRate() { return rentalRate; }
    public void setRentalRate(BigDecimal rentalRate) { this.rentalRate = rentalRate; }
    public Integer getLength() { return length; }
    public void setLength(Integer length) { this.length = length; }
    public BigDecimal getReplacementCost() { return replacementCost; }
    public void setReplacementCost(BigDecimal replacementCost) { this.replacementCost = replacementCost; }
    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }
    public String getSpecialFeatures() { return specialFeatures; }
    public void setSpecialFeatures(String specialFeatures) { this.specialFeatures = specialFeatures; }
    public List<Integer> getActorIds() { return actorIds; }
    public void setActorIds(List<Integer> actorIds) { this.actorIds = actorIds; }

    @Override
    public String toString() {
        return "FilmDTO{filmId=" + filmId + ", title='" + title + "', languageId=" + languageId + ", actorIds=" + actorIds + "}";
    }
}