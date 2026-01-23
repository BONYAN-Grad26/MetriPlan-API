package com.abdelaziz26.metriplate.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "meal_ingredients")
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
    @JoinColumn(nullable = false, name = "meal-id")
    private Meal meal;

    @ManyToOne
    @JoinColumn(nullable = false, name = "ingredient-id")
    private Ingredient ingredient;
}
