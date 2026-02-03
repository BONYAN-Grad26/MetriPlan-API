package com.abdelaziz26.metriplate.utils.mappers;

import com.abdelaziz26.metriplate.dtos.plan.DietPlanDTO;
import com.abdelaziz26.metriplate.entities.DietPlan;
import com.abdelaziz26.metriplate.entities.Goal;
import com.abdelaziz26.metriplate.entities.User;
import com.abdelaziz26.metriplate.enums.PlanStatus;
import org.springframework.stereotype.Component;

@Component
public class DietPlanMapper {
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
}
