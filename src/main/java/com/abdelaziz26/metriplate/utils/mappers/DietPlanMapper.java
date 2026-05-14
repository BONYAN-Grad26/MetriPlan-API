package com.abdelaziz26.metriplate.utils.mappers;

import com.abdelaziz26.metriplate.dtos.plan.DietPlanDTO;
import com.abdelaziz26.metriplate.dtos.plan.DietPlanSimpleResponseDto;
import com.abdelaziz26.metriplate.entities.diet.DietPlan;
import com.abdelaziz26.metriplate.entities.user.Goal;
import com.abdelaziz26.metriplate.entities.user.User;
import com.abdelaziz26.metriplate.enums.PlanStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DietPlanMapper {

    private final WeeklyPlanMapper weeklyPlanMapper;

    public DietPlan toEntity(DietPlanDTO dto, User user, Goal goal) {
        if (dto == null) return null;

        return DietPlan.builder()
                .user(user)
                .goal(goal)
                .planName(dto.getPlanName() != null ? dto.getPlanName() : "AI-Generated Plan")
                .description(dto.getDescription())
                .dailyCalorieTarget(dto.getDailyCalorieTarget())
                .proteinPercentage(dto.getProteinPercentage())
                .carbPercentage(dto.getCarbPercentage())
                .fatPercentage(dto.getFatPercentage())
                .aiSuccessTips(dto.getAiSuccessTips())
                .status(PlanStatus.GENERATED)
                .build();
    }

    public DietPlanDTO toDto(DietPlan entity) {
        if (entity == null) return null;

        DietPlanDTO dto = new DietPlanDTO();
        dto.setPlanName(entity.getPlanName());
        dto.setDescription(entity.getDescription());
        dto.setDailyCalorieTarget(entity.getDailyCalorieTarget());
        dto.setProteinPercentage(entity.getProteinPercentage());
        dto.setCarbPercentage(entity.getCarbPercentage());
        dto.setFatPercentage(entity.getFatPercentage());
        dto.setAiSuccessTips(entity.getAiSuccessTips());
        dto.setWeeks(entity.getWeeklyPlans().stream().map(weeklyPlanMapper::toDto).collect(Collectors.toList()));

        return dto;
    }

    public DietPlanSimpleResponseDto toSimpleDto(DietPlan entity) {
        if (entity == null) return null;

        DietPlanSimpleResponseDto dto = new DietPlanSimpleResponseDto();
        dto.setId(entity.getId());
        dto.setPlanName(entity.getPlanName());
        dto.setDescription(entity.getDescription());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setDurationInWeeks(entity.getDurationInWeeks());
        dto.setDietType(entity.getDietType().toString());
        dto.setMealsPerDay(entity.getMealsPerDay());
        dto.setIncludeSnacks(entity.getIncludeSnacks());
        dto.setDailyCalorieTarget(entity.getDailyCalorieTarget());
        dto.setProteinPercentage(entity.getProteinPercentage());
        dto.setCarbPercentage(entity.getCarbPercentage());
        dto.setFatPercentage(entity.getFatPercentage());
        dto.setAiSuccessTips(entity.getAiSuccessTips());
        dto.setStatus(entity.getStatus().toString());

        return dto;
    }
}
