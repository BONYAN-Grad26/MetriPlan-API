package com.abdelaziz26.metriplate.dtos.allergy;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReadAllergyDto {

    private Long id;

    private String type;

    private String name;

    private String description;

    private String nutrientName;
}
