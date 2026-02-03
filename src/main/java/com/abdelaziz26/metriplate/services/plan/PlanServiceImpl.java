package com.abdelaziz26.metriplate.services.plan;

import com.abdelaziz26.metriplate.dtos.plan.*;
import com.abdelaziz26.metriplate.entities.*;
import com.abdelaziz26.metriplate.repositories.*;
import com.abdelaziz26.metriplate.utils.MacroCalculator;
import com.abdelaziz26.metriplate.utils.mappers.DailyPlanMapper;
import com.abdelaziz26.metriplate.utils.mappers.DietPlanMapper;
import com.abdelaziz26.metriplate.utils.mappers.MealMapper;
import com.abdelaziz26.metriplate.utils.mappers.WeeklyPlanMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final DietPlanRepository dietPlanRepo;
    private final WeeklyPlanRepository weeklyPlanRepo;
    private final DailyPlanRepository dailyPlanRepo;
    private final MealRepository mealRepo;
    private final IngredientRepository ingredientRepo;
    private final DailyPlanMealRepository dailyPlanMealRepo;

    private final DietPlanMapper dietPlanMapper;
    private final WeeklyPlanMapper weeklyPlanMapper;
    private final MealMapper mealMapper;
    private final DailyPlanMapper dailyPlanMapper;

    private final MacroCalculator macroCalculator;

    Map<String, Ingredient> ingredientMap = new HashMap<>();  // need to be filled


    // ----_____________------ need to be refactored -> use cascade
    public DietPlan persistPlans(DietPlanDTO planDto, User user, Goal goal) {

        DietPlan dietPlan = dietPlanMapper.toEntity(planDto, user, goal);
        DietPlan savedPlan = dietPlanRepo.save(dietPlan);

        for(WeekDTO wpDto : planDto.getWeeks()) {
            WeeklyPlan wp = weeklyPlanMapper.toEntity(wpDto, savedPlan);
            WeeklyPlan savedWeek = weeklyPlanRepo.save(wp);

            for(DayDTO dpDto : wpDto.getDays()) {
                DailyPlan dp = dailyPlanMapper.toEntity(dpDto, savedWeek);
                DailyPlan savedDp = dailyPlanRepo.save(dp);

                int mealOrder = 1;
                for (MealDTO mealDto : dpDto.getMeals()) {
                    Meal meal = mealMapper.toEntity(mealDto);

                    List<MealIngredient> mealIngredients = createMealIngredients(mealDto, ingredientMap);
                    meal.setIngredients(mealIngredients);

                    Meal savedMeal = mealRepo.save(meal);

                    DailyPlanMeal link = new DailyPlanMeal();
                    link.setDailyPlan(savedDp);
                    link.setMeal(savedMeal);
                    link.setMealOrder(mealOrder);
                    link.setIsCustomized(false);
                    dailyPlanMealRepo.save(link);

                    mealOrder++;
                }
            }
        }

        return dietPlan;
    }


    private List<MealIngredient> createMealIngredients(MealDTO mealDto, Map<String, Ingredient> ingredients) {
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
