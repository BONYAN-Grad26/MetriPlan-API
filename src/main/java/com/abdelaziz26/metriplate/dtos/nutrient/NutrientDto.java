package com.abdelaziz26.metriplate.dtos.nutrient;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class NutrientDto {
    private Long id;
    private String name;
    private String nutrientType;
}
