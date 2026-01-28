package com.abdelaziz26.metriplate.dtos.nutrient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

@Getter @Setter @AllArgsConstructor @Builder
public class ReadNutrientDto {

    private Long id;
    private String name;
    private String nutrientType;
    private String unit;
    private String description;

    private Double rdaValue;
    private String rdaUnit;
}
