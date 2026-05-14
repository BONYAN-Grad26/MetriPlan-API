package com.abdelaziz26.metriplate.utils.mappers;

import com.abdelaziz26.metriplate.dtos.plan.DayDTO;
import com.abdelaziz26.metriplate.dtos.plan.IngredientDTO;
import com.abdelaziz26.metriplate.dtos.plan.MealDTO;
import com.abdelaziz26.metriplate.entities.diet.DailyPlan;
import com.abdelaziz26.metriplate.entities.diet.DailyPlanMeal;
import com.abdelaziz26.metriplate.entities.diet.MealIngredient;
import com.abdelaziz26.metriplate.entities.diet.WeeklyPlan;
import com.abdelaziz26.metriplate.enums.DayStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Component
public class DailyPlanMapper {
    public DailyPlan toEntity(DayDTO dto, WeeklyPlan weeklyPlan) {
        if (dto == null) return null;

        return DailyPlan.builder()
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
                .status(DayStatus.PLANNED)
                .build();
    }

    private LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            return LocalDate.now();
        }
    }



    public DayDTO toDto(DailyPlan entity) {
        if (entity == null) return null;

        DayDTO dto = new DayDTO();
        dto.setDate(entity.getDate().toString());
        dto.setDayOfWeek(entity.getDayOfWeek());
        dto.setTargetCalories(entity.getTargetCalories());
        dto.setTargetProtein(entity.getTargetProtein());
        dto.setTargetCarbs(entity.getTargetCarbs());
        dto.setTargetFat(entity.getTargetFat());
        dto.setAiDailyTips(entity.getAiDailyTips());
        dto.setMeals(entity.getMeals().stream().map(this::toMealDto).collect(Collectors.toList()));

        return dto;
    }

    private MealDTO toMealDto(DailyPlanMeal dailyPlanMeal) {
        if (dailyPlanMeal == null) return null;

        MealDTO mealDto = new MealDTO();
        mealDto.setName(dailyPlanMeal.getMeal().getName());
        mealDto.setMealType(dailyPlanMeal.getMeal().getMealType().toString());
        //mealDto.setTotalCalories((int) dailyPlanMeal.getMeal().getNutritionalInfo().getCalories());
        //mealDto.setTotalProtein((int) dailyPlanMeal.getMeal().getNutritionalInfo().getProtein());
        //mealDto.setTotalCarbs((int) dailyPlanMeal.getMeal().getNutritionalInfo().getCarbohydrates());
        //mealDto.setTotalFat((int) dailyPlanMeal.getMeal().getNutritionalInfo().getFat());
        //mealDto.setRecipe(dailyPlanMeal.getMeal().getRecipe());
        //mealDto.setExtraInfo(dailyPlanMeal.getMeal().getExtraInfo());
        mealDto.setIngredients(dailyPlanMeal.getMeal().getIngredients().stream().map(this::toIngredientDto).collect(Collectors.toList()));

        return mealDto;
    }

    private IngredientDTO toIngredientDto(MealIngredient mealIngredient) {
        if (mealIngredient == null) return null;

        IngredientDTO ingredientDto = new IngredientDTO();
        ingredientDto.setName(mealIngredient.getIngredient().getName());
        ingredientDto.setQuantity(mealIngredient.getQuantity());
        return ingredientDto;
    }
}
