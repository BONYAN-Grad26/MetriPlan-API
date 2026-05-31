package com.abdelaziz26.metriplate.mappers;

import com.abdelaziz26.metriplate.dtos.plan.DayDTO;
import com.abdelaziz26.metriplate.dtos.plan.IngredientDTO;
import com.abdelaziz26.metriplate.dtos.plan.MealDTO;
import com.abdelaziz26.metriplate.dtos.plan.WeekDTO;
import com.abdelaziz26.metriplate.entities.diet.*;
import com.abdelaziz26.metriplate.enums.diet.MealType;
import com.abdelaziz26.metriplate.repositories.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WeeklyPlanMapper {

    private final IngredientRepository ingredientRepository;

    // ════════════════════════════════════════════════════════════════
    // DTO MAPPINGS (Entity -> DTO)
    // ════════════════════════════════════════════════════════════════

    public WeekDTO toDto(WeeklyPlan entity) {

        if (entity == null) return null;

        WeekDTO dto = new WeekDTO();
        dto.setId(entity.getId());
        dto.setWeekNumber(entity.getWeekNumber());
        dto.setStartDate(entity.getStartDate() != null ? entity.getStartDate().toString() : null);
        dto.setEndDate(entity.getEndDate() != null ? entity.getEndDate().toString() : null);
        dto.setWeeklyCalorieTarget(entity.getWeeklyCalorieTarget());
        dto.setWeeklyProteinTarget(entity.getWeeklyProteinTarget());
        dto.setWeeklyCarbTarget(entity.getWeeklyCarbTarget());
        dto.setWeeklyFatTarget(entity.getWeeklyFatTarget());
        dto.setWeeklyStrategy(entity.getWeeklyStrategy());
        dto.setAiPreparationTips(entity.getAiPreparationTips());

        if (entity.getDailyPlans() != null) {
            List<DayDTO> days = entity.getDailyPlans()
                    .stream()
                    .map(this::toDayDto)
                    .toList();
            dto.setDays(days);
        }

        return dto;
    }

    public DayDTO toDayDto(DailyPlan entity) {

        if (entity == null) return null;

        DayDTO dto = new DayDTO();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate() != null ? entity.getDate().toString() : null);
        dto.setDayOfWeek(entity.getDayOfWeek());
        dto.setTargetCalories(entity.getTargetCalories());
        dto.setTargetProtein(entity.getTargetProtein());
        dto.setTargetCarbs(entity.getTargetCarbs());
        dto.setTargetFat(entity.getTargetFat());
        dto.setTargetFiber(entity.getTargetFiber());
        dto.setTargetSugar(entity.getTargetSugar());
        dto.setWaterGoal(entity.getWaterGoal());
        dto.setAiDailyTips(entity.getAiDailyTips());

        if (entity.getMeals() != null) {
            List<MealDTO> meals = entity.getMeals()
                    .stream()
                    .map(this::toMealDto)
                    .toList();
            dto.setMeals(meals);
        }

        return dto;
    }

    private MealDTO toMealDto(DailyPlanMeal entity) {

        if (entity == null) return null;

        Meal meal = entity.getMeal();

        MealDTO dto = new MealDTO();
        dto.setId(meal != null ? meal.getId() : null);
        dto.setName(meal != null ? meal.getName() : null);
        dto.setMealType(meal != null && meal.getMealType() != null ? meal.getMealType().name() : null);
        dto.setDescription(meal != null ? meal.getDescription() : null);
        dto.setPreparationTime(meal != null ? meal.getPreparationTime() : null);
        dto.setPreparationInstructions(meal != null ? meal.getPreparationInstructions() : null);
        dto.setOrder(entity.getMealOrder());

        if (meal != null && meal.getIngredients() != null) {
            List<IngredientDTO> ingredients = meal.getIngredients()
                    .stream()
                    .map(this::toIngredientDto)
                    .toList();
            dto.setIngredients(ingredients);
        }

        return dto;
    }

    private IngredientDTO toIngredientDto(MealIngredient entity) {

        if (entity == null) return null;

        Ingredient ing = entity.getIngredient();

        IngredientDTO dto = new IngredientDTO();
        dto.setIngredientId(ing != null ? ing.getId() : null);
        dto.setIngredientName(ing != null ? ing.getName() : null);
        dto.setQuantity(entity.getQuantity());
        dto.setMeasurementUnit(entity.getMeasurementUnit());
        //dto.setCalories(entity.getCalories());
        //dto.setProtein(entity.getProtein());
        //dto.setCarbs(entity.getCarbs());
        //dto.setFat(entity.getFat());

        return dto;
    }

    // ════════════════════════════════════════════════════════════════
    // ROOT MAPPING
    // ════════════════════════════════════════════════════════════════

    public WeeklyPlan toEntity(WeekDTO dto) {

        if (dto == null) {
            return null;
        }

        WeeklyPlan weeklyPlan = WeeklyPlan.builder()
                .weekNumber(dto.getWeekNumber())
                .startDate(parseDate(dto.getStartDate()))
                .endDate(parseDate(dto.getEndDate()))
                .weeklyCalorieTarget(dto.getWeeklyCalorieTarget())
                .weeklyProteinTarget(dto.getWeeklyProteinTarget())
                .weeklyCarbTarget(dto.getWeeklyCarbTarget())
                .weeklyFatTarget(dto.getWeeklyFatTarget())
                .weeklyStrategy(dto.getWeeklyStrategy())
                .aiPreparationTips(dto.getAiPreparationTips())
                .dailyPlans(new ArrayList<>())
                .build();

        if (dto.getDays() != null) {

            List<DailyPlan> dailyPlans = dto.getDays()
                    .stream()
                    .map(dayDto -> toDailyPlanEntity(dayDto, weeklyPlan))
                    .toList();

            weeklyPlan.getDailyPlans().addAll(dailyPlans);
        }

        return weeklyPlan;
    }

    // ════════════════════════════════════════════════════════════════
    // DAILY PLAN
    // ════════════════════════════════════════════════════════════════

    private DailyPlan toDailyPlanEntity(
            DayDTO dto,
            WeeklyPlan weeklyPlan
    ) {

        DailyPlan dailyPlan = DailyPlan.builder()
                .weeklyPlan(weeklyPlan)
                .date(parseDate(dto.getDate()))
                .dayOfWeek(dto.getDayOfWeek())
                .targetCalories(dto.getTargetCalories())
                .targetProtein(dto.getTargetProtein())
                .targetCarbs(dto.getTargetCarbs())
                .targetFat(dto.getTargetFat())
                .targetFiber(dto.getTargetFiber())
                .targetSugar(dto.getTargetSugar())
                .waterGoal(dto.getWaterGoal())
                .aiDailyTips(dto.getAiDailyTips())
                .meals(new ArrayList<>())
                .build();

        if (dto.getMeals() != null) {

            List<DailyPlanMeal> meals = dto.getMeals()
                    .stream()
                    .map(mealDto -> toDailyPlanMealEntity(
                            mealDto,
                            dailyPlan
                    ))
                    .toList();

            dailyPlan.getMeals().addAll(meals);
        }

        return dailyPlan;
    }

    // ════════════════════════════════════════════════════════════════
    // DAILY PLAN MEAL
    // ════════════════════════════════════════════════════════════════

    private DailyPlanMeal toDailyPlanMealEntity(
            MealDTO dto,
            DailyPlan dailyPlan
    ) {

        Meal meal = toMealEntity(dto);

        DailyPlanMeal dailyPlanMeal = new DailyPlanMeal();
        dailyPlanMeal.setDailyPlan(dailyPlan);
        dailyPlanMeal.setMeal(meal);
        dailyPlanMeal.setMealOrder(dto.getOrder());
        dailyPlanMeal.setIsCustomized(false);

        return dailyPlanMeal;
    }

    // ════════════════════════════════════════════════════════════════
    // MEAL
    // ════════════════════════════════════════════════════════════════

    private Meal toMealEntity(MealDTO dto) {

        Meal meal = Meal.builder()
                .name(dto.getName())
                .mealType(MealType.valueOf(dto.getMealType()))
                .description(dto.getDescription())
                .preparationTime(dto.getPreparationTime())
                .preparationInstructions(dto.getPreparationInstructions())
                .ingredients(new ArrayList<>())
                .build();

        if (dto.getIngredients() != null) {

            List<MealIngredient> ingredients = dto.getIngredients()
                    .stream()
                    .map(ingredientDto ->
                            toMealIngredientEntity(
                                    ingredientDto,
                                    meal
                            )
                    )
                    .toList();

            meal.getIngredients().addAll(ingredients);
        }

        return meal;
    }

    // ════════════════════════════════════════════════════════════════
    // MEAL INGREDIENT
    // ════════════════════════════════════════════════════════════════

    private MealIngredient toMealIngredientEntity(
            IngredientDTO dto,
            Meal meal
    ) {

        Ingredient ingredient = resolveIngredient(dto);

        return MealIngredient.builder()
                .meal(meal)
                .ingredient(ingredient)
                .quantity(dto.getQuantity())
                .measurementUnit(dto.getMeasurementUnit())
                //.calories(dto.getCalories())
                //.protein(dto.getProtein())
                //.carbs(dto.getCarbs())
                //.fat(dto.getFat())
                .build();
    }

    // ════════════════════════════════════════════════════════════════
    // INGREDIENT RESOLUTION
    // ════════════════════════════════════════════════════════════════

    private Ingredient resolveIngredient(IngredientDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException("IngredientDTO cannot be null");
        }

        if (dto.getIngredientId() != null) {

            return ingredientRepository.findById(dto.getIngredientId())
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Ingredient not found with ID: "
                                            + dto.getIngredientId()
                            )
                    );
        }

        if (dto.getIngredientName() != null) {

            return ingredientRepository
                    .findByNameIgnoreCase(
                            dto.getIngredientName().trim()
                    )
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Ingredient not found with name: "
                                            + dto.getIngredientName()
                            )
                    );
        }

        throw new IllegalArgumentException(
                "Ingredient must contain either ID or name"
        );
    }

    // ════════════════════════════════════════════════════════════════
    // UTIL
    // ════════════════════════════════════════════════════════════════

    private LocalDate parseDate(String value) {

        if (value == null || value.isBlank()) {
            return LocalDate.now();
        }

        try {
            return LocalDate.parse(
                    value,
                    DateTimeFormatter.ISO_LOCAL_DATE
            );
        }
        catch (Exception e) {
            return LocalDate.now();
        }
    }
}