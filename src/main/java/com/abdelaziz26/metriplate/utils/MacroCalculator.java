package com.abdelaziz26.metriplate.utils;

import com.abdelaziz26.metriplate.entities.Ingredient;
import org.springframework.stereotype.Component;

@Component
public class MacroCalculator {

    public double[] calculate(Ingredient ingredient, double quantityGrams) {
        double factor = quantityGrams / 100.0;

        double calories = safeVal(ingredient.getCalories())      * factor;
        double protein  = safeVal(ingredient.getProtein())       * factor;
        double carbs    = safeVal(ingredient.getCarbohydrates()) * factor;
        double fat      = safeVal(ingredient.getFat())           * factor;

        return new double[] { calories, protein, carbs, fat };
    }


    public double[] sumMeal(java.util.List<com.abdelaziz26.metriplate.entities.MealIngredient> mealIngredients) {
        double[] totals = new double[4];
        for (var mi : mealIngredients) {
            totals[0] += safeVal(mi.getCalories());
            totals[1] += safeVal(mi.getProtein());
            totals[2] += safeVal(mi.getCarbs());
            totals[3] += safeVal(mi.getFat());
        }
        return totals;
    }

    private static double safeVal(Double d) { return d == null ? 0.0 : d; }
}
