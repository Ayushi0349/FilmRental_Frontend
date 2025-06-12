package com.filmrentalfrontend.model.dto;

public class Team {
    private String firstName;
    private String iconClass;

    public Team(String firstName, String iconClass) {
        this.firstName = firstName;
        this.iconClass = iconClass;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }
}