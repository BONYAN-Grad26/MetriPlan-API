package com.abdelaziz26.metriplate.dtos.plan;

import lombok.*;

/**
 * DTO used inside MealDTO to represent an ingredient entry.
 * Expanded to include ingredientId and nutrition fields so the client
 * and server can exchange a complete representation without additional lookups.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientDTO {

    // Optional ID that should match the Ingredient entity id in the database
    private Long ingredientId;

    // Exact ingredient name (must match DB name when provided)
    private String ingredientName;

    // Quantity (numeric) - e.g., 80
    private Double quantity;

    // Measurement unit - must be one of: g, ml, kg, l, cup, tbsp, tsp, piece, oz, lb
    private String measurementUnit;

    //// Nutrition values for the provided quantity (optional but useful)
    //private Double calories;
    //private Double protein;
    //private Double carbs;
    //private Double fat;

}
