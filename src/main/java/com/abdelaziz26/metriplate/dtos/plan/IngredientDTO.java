package com.abdelaziz26.metriplate.dtos.plan;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class IngredientDTO {
    private String name;
    private Double quantity;
    private String unit;
}
