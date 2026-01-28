package com.abdelaziz26.metriplate.dtos.meal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter
@Setter
public class CreateMealDto {

    @NotBlank(message = "Meal name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Meal type is required")
    private String mealType;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @Size(max = 2000, message = "Instructions must be at most 2000 characters")
    private String preparationInstructions;

    @NotNull(message = "Preparation time is required")
    private Integer preparationTime; // in minutes

    @NotNull(message = "Ingredients are required")
    @Size(min = 1, message = "At least one ingredient is required")
    private List<MealIngredientDto> ingredients;
}
