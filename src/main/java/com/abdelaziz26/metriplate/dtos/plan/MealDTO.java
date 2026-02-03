package com.abdelaziz26.metriplate.dtos.plan;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MealDTO {

    private String name;
    private String mealType;
    private String description;
    private Integer preparationTime;
    private String preparationInstructions;

    private List<IngredientDTO> ingredients;
}
