package com.abdelaziz26.metriplate.services.meal;

import com.abdelaziz26.metriplate.dtos.plan.IngredientDTO;
import com.abdelaziz26.metriplate.dtos.plan.MealDTO;
import com.abdelaziz26.metriplate.entities.Ingredient;
import com.abdelaziz26.metriplate.entities.MealIngredient;
import com.abdelaziz26.metriplate.utils.MacroCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MealServiceImpl implements MealService {

    private final MacroCalculator macroCalculator;

    @Override
    public List<MealIngredient> createMealIngredients(MealDTO mealDto, Map<String, Ingredient> ingredients) {
        List<MealIngredient> mealIngredients = new ArrayList<>();

        for (IngredientDTO ingDto : mealDto.getIngredients()) {
            // Look up ingredient in catalogue
            Ingredient ingredient = ingredients.get(ingDto.getName().toLowerCase(Locale.ROOT));

            // Skip if ingredient not found
            if (ingredient == null) {
                log.warn("Ingredient not found in catalogue: {}", ingDto.getName());
                continue;
            }

            double[] totals = macroCalculator.calculate(ingredient, ingDto.getQuantity());

            // Create MealIngredient
            MealIngredient mealIngredient = MealIngredient.builder()
                    .ingredient(ingredient)
                    .quantity(ingDto.getQuantity())
                    .measurementUnit(ingDto.getUnit())
                    .calories(totals[0])
                    .protein(totals[1])
                    .carbs(totals[2])
                    .fat(totals[3])
                    .build();

            mealIngredients.add(mealIngredient);
        }

        return mealIngredients;
    }
}
