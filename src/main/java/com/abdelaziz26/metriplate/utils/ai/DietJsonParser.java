package com.abdelaziz26.metriplate.utils.ai;

import com.abdelaziz26.metriplate.dtos.plan.DayDTO;
import com.abdelaziz26.metriplate.dtos.plan.IngredientDTO;
import com.abdelaziz26.metriplate.dtos.plan.MealDTO;
import com.abdelaziz26.metriplate.dtos.plan.WeekDTO;
import com.abdelaziz26.metriplate.exceptions.PlanGenerationException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DietJsonParser {

    private final ObjectMapper objectMapper;

    public WeekDTO parseWeeklyPlanResponse(String jsonResponse, LocalDate startDate, int weekNumber)
            throws IOException, PlanGenerationException {

        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        if (rootNode.has("error")) {
            throw new PlanGenerationException("LLM returned error: " + rootNode.get("reason").asText());
        }

        WeekDTO weekDto = new WeekDTO();

        if (rootNode.has("weekNumber")) {
            weekDto.setWeekNumber(rootNode.get("weekNumber").asInt());
        } else {
            weekDto.setWeekNumber(weekNumber);
        }

        if (rootNode.has("startDate")) {
            weekDto.setStartDate(rootNode.get("startDate").asText());
        } else {
            weekDto.setStartDate(startDate.toString());
        }

        if (rootNode.has("endDate")) {
            weekDto.setEndDate(rootNode.get("endDate").asText());
        } else {
            LocalDate endDate = startDate.plusDays(6);
            weekDto.setEndDate(endDate.toString());
        }

        if (rootNode.has("weeklyCalorieTarget")) {
            weekDto.setWeeklyCalorieTarget(rootNode.get("weeklyCalorieTarget").asDouble());
        }
        if (rootNode.has("weeklyProteinTarget")) {
            weekDto.setWeeklyProteinTarget(rootNode.get("weeklyProteinTarget").asDouble());
        }
        if (rootNode.has("weeklyCarbTarget")) {
            weekDto.setWeeklyCarbTarget(rootNode.get("weeklyCarbTarget").asDouble());
        }
        if (rootNode.has("weeklyFatTarget")) {
            weekDto.setWeeklyFatTarget(rootNode.get("weeklyFatTarget").asDouble());
        }
        if (rootNode.has("weeklyStrategy")) {
            weekDto.setWeeklyStrategy(rootNode.get("weeklyStrategy").asText());
        }
        if (rootNode.has("aiPreparationTips")) {
            weekDto.setAiPreparationTips(rootNode.get("aiPreparationTips").asText());
        }

        List<DayDTO> days = new ArrayList<>();
        JsonNode daysNode = rootNode.get("days");
        if (daysNode != null && daysNode.isArray()) {
            for (JsonNode dayNode : daysNode) {
                DayDTO dayDto = parseDay(dayNode);
                days.add(dayDto);
            }
        }
        weekDto.setDays(days);

        return weekDto;
    }

    public DayDTO parseDay(JsonNode dayNode) throws IOException {
        DayDTO dayDto = new DayDTO();

        if (dayNode.has("date")) {
            dayDto.setDate(dayNode.get("date").asText());
        }
        if (dayNode.has("dayOfWeek")) {
            dayDto.setDayOfWeek(dayNode.get("dayOfWeek").asInt());
        }
        if (dayNode.has("targetCalories")) {
            dayDto.setTargetCalories(dayNode.get("targetCalories").asDouble());
        }
        if (dayNode.has("targetProtein")) {
            dayDto.setTargetProtein(dayNode.get("targetProtein").asDouble());
        }
        if (dayNode.has("targetCarbs")) {
            dayDto.setTargetCarbs(dayNode.get("targetCarbs").asDouble());
        }
        if (dayNode.has("targetFat")) {
            dayDto.setTargetFat(dayNode.get("targetFat").asDouble());
        }
        if (dayNode.has("targetFiber")) {
            dayDto.setTargetFiber(dayNode.get("targetFiber").asDouble());
        }
        if (dayNode.has("targetSugar")) {
            dayDto.setTargetSugar(dayNode.get("targetSugar").asDouble());
        }
        if (dayNode.has("waterGoal")) {
            dayDto.setWaterGoal(dayNode.get("waterGoal").asDouble());
        }
        if (dayNode.has("aiDailyTips")) {
            dayDto.setAiDailyTips(dayNode.get("aiDailyTips").asText());
        }

        // Parse meals using the existing DayDTO mapper or inline
        List<com.abdelaziz26.metriplate.dtos.plan.MealDTO> meals = new ArrayList<>();
        JsonNode mealsNode = dayNode.get("meals");
        if (mealsNode != null && mealsNode.isArray()) {
            for (JsonNode mealNode : mealsNode) {
                com.abdelaziz26.metriplate.dtos.plan.MealDTO mealDto = parseMeal(mealNode);
                meals.add(mealDto);
            }
        }
        dayDto.setMeals(meals);

        return dayDto;
    }

    public MealDTO parseMeal(JsonNode mealNode) throws IOException {
        com.abdelaziz26.metriplate.dtos.plan.MealDTO mealDto = new com.abdelaziz26.metriplate.dtos.plan.MealDTO();

        if (mealNode.has("name")) {
            mealDto.setName(mealNode.get("name").asText());
        }
        if (mealNode.has("mealType")) {
            mealDto.setMealType(mealNode.get("mealType").asText());
        }
        if (mealNode.has("description")) {
            mealDto.setDescription(mealNode.get("description").asText());
        }
        if (mealNode.has("preparationTime")) {
            mealDto.setPreparationTime(mealNode.get("preparationTime").asInt());
        }
        if (mealNode.has("preparationInstructions")) {
            mealDto.setPreparationInstructions(mealNode.get("preparationInstructions").asText());
        }
        if (mealNode.has("order")) {
            mealDto.setOrder(mealNode.get("order").asInt());
        }

        // Parse ingredients
        List<com.abdelaziz26.metriplate.dtos.plan.IngredientDTO> ingredients = new ArrayList<>();
        JsonNode ingredientsNode = mealNode.get("ingredients");
        if (ingredientsNode != null && ingredientsNode.isArray()) {
            for (JsonNode ingredientNode : ingredientsNode) {
                com.abdelaziz26.metriplate.dtos.plan.IngredientDTO ingredientDto = parseIngredient(ingredientNode);
                ingredients.add(ingredientDto);
            }
        }
        mealDto.setIngredients(ingredients);

        return mealDto;
    }

    public IngredientDTO parseIngredient(JsonNode ingredientNode) {
        com.abdelaziz26.metriplate.dtos.plan.IngredientDTO ingredientDto =
                new com.abdelaziz26.metriplate.dtos.plan.IngredientDTO();

        if (ingredientNode.has("ingredientId")) {
            ingredientDto.setIngredientId(ingredientNode.get("ingredientId").asLong());
        }
        if (ingredientNode.has("ingredientName")) {
            ingredientDto.setIngredientName(ingredientNode.get("ingredientName").asText());
        }
        if (ingredientNode.has("quantity")) {
            ingredientDto.setQuantity(ingredientNode.get("quantity").asDouble());
        }
        if (ingredientNode.has("measurementUnit")) {
            ingredientDto.setMeasurementUnit(ingredientNode.get("measurementUnit").asText());
        }
        if (ingredientNode.has("calories")) {
            ingredientDto.setCalories(ingredientNode.get("calories").asDouble());
        }
        if (ingredientNode.has("protein")) {
            ingredientDto.setProtein(ingredientNode.get("protein").asDouble());
        }
        if (ingredientNode.has("carbs")) {
            ingredientDto.setCarbs(ingredientNode.get("carbs").asDouble());
        }
        if (ingredientNode.has("fat")) {
            ingredientDto.setFat(ingredientNode.get("fat").asDouble());
        }

        return ingredientDto;
    }
}
