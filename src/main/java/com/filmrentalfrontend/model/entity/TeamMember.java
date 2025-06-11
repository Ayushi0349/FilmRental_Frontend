package com.filmrentalfrontend.model.entity;

import lombok.Data;

@Data
public class TeamMember {
    private Long id;
    private String name;

    public TeamMember() {
    }

    public TeamMember(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}