package com.abdelaziz26.metriplate.dtos.meal;

import lombok.*;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter
@Setter @Builder
public class MealSummaryDto {

    private Long id;
    private String name;
    private String mealType;
    private String description;
    private Integer preparationTime;

    // Quick nutritional info
    private Double calories;
    private Double protein;
    private String proteinSource; // e.g., "Chicken", "Tofu"

    // Popularity metrics
    private Double averageRating;
    private Boolean isAiGenerated;
    private Boolean isFavorite;

    // Tags for filtering
    private List<String> dietaryTags;
    private String difficulty;
    private String cuisine;

    // Quick view of ingredients
    private Integer ingredientCount;
    private List<String> mainIngredients; // First
}
