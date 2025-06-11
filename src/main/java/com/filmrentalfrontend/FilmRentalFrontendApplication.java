package com.filmrentalfrontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.filmrentalfrontend", "com.filmrental"})
public class FilmRentalFrontendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilmRentalFrontendApplication.class, args);
    }
}
