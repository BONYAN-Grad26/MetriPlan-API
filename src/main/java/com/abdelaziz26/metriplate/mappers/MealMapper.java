package com.abdelaziz26.metriplate.mappers;

import com.abdelaziz26.metriplate.dtos.plan.MealDTO;
import com.abdelaziz26.metriplate.entities.diet.Meal;
import com.abdelaziz26.metriplate.enums.diet.MealType;
import org.springframework.stereotype.Component;

@Component
public class MealMapper {
    public Meal toEntity(MealDTO dto) {
        if (dto == null) return null;

        return Meal.builder()
                .name(dto.getName())
                .mealType(MealType.valueOf(dto.getMealType()))
                .description(dto.getDescription())
                .preparationTime(dto.getPreparationTime())
                .preparationInstructions(dto.getPreparationInstructions())
                .build();
    }
}
