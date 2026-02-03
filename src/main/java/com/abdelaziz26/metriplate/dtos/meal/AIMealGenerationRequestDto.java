package com.abdelaziz26.metriplate.dtos.meal;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class AIMealGenerationRequestDto {

    @NotBlank(message = "Meal type is required")
    private String mealType;

    @Builder.Default
    private Integer numberOfMeals = 1;

    // Nutritional constraints
    private Double maxCalories;
    private Double minProtein;
    private Double maxCarbs;
    private Double maxFat;

    // Time constraints
    private Integer maxPreparationTime; // in minutes

    // Available ingredients (IDs or names)
    private List<Long> availableIngredientIds;
    private List<String> availableIngredientNames;

    // Dietary restrictions
    private List<String> dietaryRestrictions; // e.g., ["VEGAN", "GLUTEN_FREE"]
    private List<String> excludedIngredients;
}
