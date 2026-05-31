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
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DietJsonParser {

    private final ObjectMapper objectMapper;

    public WeekDTO parseWeeklyPlanResponse(String json, LocalDate startDate, int weekNumber) throws Exception {
        String cleanJson = stripMarkdownFences(json);
        JsonNode root = objectMapper.readTree(cleanJson);
        JsonNode mealPlan = root.path("meal_plan");

        if (mealPlan.isMissingNode() || !mealPlan.isArray() || mealPlan.isEmpty()) {
            throw new IllegalArgumentException("Invalid meal plan JSON: missing or empty 'meal_plan' array");
        }

        WeekDTO week = buildWeekDTO(mealPlan, startDate, weekNumber);
        week.setDays(buildDays(mealPlan));
        return week;
    }

    private String stripMarkdownFences(String raw) {
        if (raw == null) return "";
        String stripped = raw.strip();
        if (stripped.startsWith("```")) {
            stripped = stripped.replaceFirst("^```(?:json)?\\s*", "");
            stripped = stripped.replaceFirst("\\s*```$", "");
        }
        return stripped.strip();
    }

    // -------------------------------------------------------------------------
    // Week
    // -------------------------------------------------------------------------

    private WeekDTO buildWeekDTO(JsonNode mealPlan, LocalDate startDate, int weekNumber) {
        WeekDTO week = new WeekDTO();

        String firstDate = mealPlan.get(0).path("date").asText();
        String lastDate  = mealPlan.get(mealPlan.size() - 1).path("date").asText();

        week.setStartDate(firstDate.isBlank() ? startDate.toString() : firstDate);
        week.setEndDate(lastDate.isBlank() ? startDate.plusDays(6).toString() : lastDate);
        week.setWeekNumber(firstDate.isBlank() ? weekNumber : resolveWeekNumber(firstDate));

        double totalCalories = 0, totalProtein = 0, totalCarbs = 0, totalFat = 0;
        for (JsonNode day : mealPlan) {
            JsonNode totals = day.path("daily_totals");
            totalCalories += totals.path("calories").asDouble();
            totalProtein  += totals.path("protein").asDouble();
            totalCarbs    += totals.path("carbs").asDouble();
            totalFat      += totals.path("fat").asDouble();
        }

        week.setWeeklyCalorieTarget(totalCalories);
        week.setWeeklyProteinTarget(totalProtein);
        week.setWeeklyCarbTarget(totalCarbs);
        week.setWeeklyFatTarget(totalFat);

        return week;
    }

    private int resolveWeekNumber(String isoDate) {
        LocalDate date = LocalDate.parse(isoDate, DateTimeFormatter.ISO_LOCAL_DATE);
        return date.get(WeekFields.ISO.weekOfWeekBasedYear());
    }

    // -------------------------------------------------------------------------
    // Days
    // -------------------------------------------------------------------------

    private List<DayDTO> buildDays(JsonNode mealPlan) {
        List<DayDTO> days = new ArrayList<>();
        int dayIndex = 1;
        for (JsonNode dayNode : mealPlan) {
            days.add(buildDayDTO(dayNode, dayIndex++));
        }
        return days;
    }

    private DayDTO buildDayDTO(JsonNode dayNode, int dayIndex) {
        DayDTO day = new DayDTO();

        day.setDate(dayNode.path("date").asText());
        day.setDayOfWeek(dayIndex);

        JsonNode totals = dayNode.path("daily_totals");
        day.setTargetCalories(totals.path("calories").asDouble());
        day.setTargetProtein(totals.path("protein").asDouble());
        day.setTargetCarbs(totals.path("carbs").asDouble());
        day.setTargetFat(totals.path("fat").asDouble());
        day.setTargetFiber(totals.path("fiber").asDouble());
        day.setTargetSugar(totals.path("sugar").asDouble());
        day.setWaterGoal(totals.path("water_intake").asDouble());

        day.setMeals(buildMeals(dayNode.path("meals")));
        return day;
    }

    // -------------------------------------------------------------------------
    // Meals
    // -------------------------------------------------------------------------

    private List<MealDTO> buildMeals(JsonNode mealsNode) {
        List<MealDTO> meals = new ArrayList<>();
        int order = 1;
        for (JsonNode mealNode : mealsNode) {
            meals.add(buildMealDTO(mealNode, order++));
        }
        return meals;
    }

    private MealDTO buildMealDTO(JsonNode mealNode, int order) {
        MealDTO meal = new MealDTO();

        meal.setName(mealNode.path("name").asText());
        meal.setMealType(mealNode.path("meal_type").asText());
        meal.setPreparationInstructions(mealNode.path("instructions").asText());
        meal.setOrder(order);

        JsonNode macros = mealNode.path("macros");
        if (!macros.isMissingNode()) {
            meal.setDescription(buildMacroDescription(macros));
        }

        meal.setIngredients(buildIngredients(mealNode.path("ingredients")));
        return meal;
    }

    private String buildMacroDescription(JsonNode macros) {
        return String.format(
                "Calories: %.1f | Protein: %.1fg | Carbs: %.1fg | Fat: %.1fg",
                macros.path("calories").asDouble(),
                macros.path("protein").asDouble(),
                macros.path("carbs").asDouble(),
                macros.path("fat").asDouble()
        );
    }

    // -------------------------------------------------------------------------
    // Ingredients
    // -------------------------------------------------------------------------

    private List<IngredientDTO> buildIngredients(JsonNode ingredientsNode) {
        List<IngredientDTO> ingredients = new ArrayList<>();
        for (JsonNode ingNode : ingredientsNode) {
            ingredients.add(buildIngredientDTO(ingNode));
        }
        return ingredients;
    }

    private IngredientDTO buildIngredientDTO(JsonNode ingNode) {
        IngredientDTO ing = new IngredientDTO();

        if (ingNode.hasNonNull("id")) {
            ing.setIngredientId(ingNode.path("id").asLong());
        }

        ing.setIngredientName(ingNode.path("name").asText());
        ing.setQuantity(ingNode.path("quantity").asDouble());
        ing.setMeasurementUnit(normalizeUnit(ingNode.path("unit").asText()));

        //if (ingNode.hasNonNull("calories")) ing.setCalories(ingNode.path("calories").asDouble());
        //if (ingNode.hasNonNull("protein"))  ing.setProtein(ingNode.path("protein").asDouble());
        //if (ingNode.hasNonNull("carbs"))    ing.setCarbs(ingNode.path("carbs").asDouble());
        //if (ingNode.hasNonNull("fat"))      ing.setFat(ingNode.path("fat").asDouble());

        return ing;
    }

    private String normalizeUnit(String raw) {
        if (raw == null) return "g";
        return switch (raw.trim().toLowerCase()) {
            case "ml", "milliliter", "millilitre" -> "ml";
            case "kg", "kilogram"                 -> "kg";
            case "l", "liter", "litre"            -> "l";
            case "cup", "cups"                    -> "cup";
            case "tbsp", "tablespoon"             -> "tbsp";
            case "tsp", "teaspoon"                -> "tsp";
            case "piece", "pieces", "pcs", "pc"  -> "piece";
            case "oz", "ounce"                    -> "oz";
            case "lb", "pound"                    -> "lb";
            default                               -> "g";
        };
    }
}
