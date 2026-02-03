package com.abdelaziz26.metriplate.services.meal;

import com.abdelaziz26.metriplate.dtos.plan.MealDTO;
import com.abdelaziz26.metriplate.entities.Ingredient;
import com.abdelaziz26.metriplate.entities.MealIngredient;

import java.util.List;
import java.util.Map;

public interface MealService {
    List<MealIngredient> createMealIngredients(MealDTO mealDto, Map<String, Ingredient> ingredients);
}
