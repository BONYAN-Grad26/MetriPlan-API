package com.abdelaziz26.metriplate.dtos.ingredients;

import lombok.*;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class IngredientDto {

    private Long id;

    private String name;

    private String imageUrl;

    private BigDecimal price;
}
