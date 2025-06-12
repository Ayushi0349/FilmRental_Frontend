package com.filmrentalfrontend.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class PageDTO<T> {
    private List<T> content;
    private int number;
    private int size;
    private long totalElements;
    private int totalPages;
    private int numberOfElements;
}