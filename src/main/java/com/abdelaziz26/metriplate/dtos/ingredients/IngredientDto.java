package com.abdelaziz26.metriplate.dtos.ingredients;

import lombok.*;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class IngredientDto {

    private Long id;

    private String name;
}
