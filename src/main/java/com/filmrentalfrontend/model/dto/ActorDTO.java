package com.filmrentalfrontend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActorDTO {
    private Integer actorId;
    private String firstName;
    private String lastName;
    private List<Integer> filmIds;
}