package com.abdelaziz26.metriplate.entities.diet;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "meal_ingredients")
@Builder
public class MealIngredient {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fat;

    private Double quantity; // in grams
    private String measurementUnit; // g, ml, cups, pieces, etc.

    @ManyToOne
    @JoinColumn(nullable = false, name = "meal_id")
    private Meal meal;

    @ManyToOne
    @JoinColumn(nullable = false, name = "ingredient_id")
    private Ingredient ingredient;
}
