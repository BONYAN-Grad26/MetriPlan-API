package com.abdelaziz26.metriplate.dtos.plan;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MealDTO {
    // Optional id (used when mapping to existing Meal entity)
    private Long id;

    private String name;
    private String mealType;
    private String description;
    private Integer preparationTime;
    private String preparationInstructions;

    // Order of the meal within the day (1..n)
    private Integer order;

    private List<IngredientDTO> ingredients;
}
