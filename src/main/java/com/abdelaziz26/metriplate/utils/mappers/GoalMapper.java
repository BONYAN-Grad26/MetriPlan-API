package com.abdelaziz26.metriplate.utils.mappers;

import com.abdelaziz26.metriplate.dtos.goal.CreateGoalDto;
import com.abdelaziz26.metriplate.dtos.goal.GoalSummaryDto;
import com.abdelaziz26.metriplate.dtos.goal.ReadGoalDto;
import com.abdelaziz26.metriplate.dtos.goal.UpdateGoalDto;
import com.abdelaziz26.metriplate.entities.Goal;
import com.abdelaziz26.metriplate.entities.User;
import com.abdelaziz26.metriplate.enums.GoalType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class GoalMapper {

    public Goal toEntity(CreateGoalDto dto, User user) {
        return Goal.builder()
                .user(user)
                .goalType(GoalType.valueOf(dto.getGoalType().toUpperCase()))
                .description(dto.getDescription())
                .targetWeight(dto.getTargetWeight())
                .targetBodyFat(dto.getTargetBodyFat())
                .targetCalories(null)  // logic needed here
                .targetDate(dto.getTargetDate())
                .startDate(LocalDate.now())
                .build();
    }

    public Goal toEntity(UpdateGoalDto dto, Goal goal) {

        if (dto.getGoalType() != null) {
            goal.setGoalType(
                    GoalType.valueOf(dto.getGoalType().toUpperCase())
            );
        }

        if (dto.getDescription() != null) {
            goal.setDescription(dto.getDescription());
        }

        if (dto.getTargetWeight() != null) {
            goal.setTargetWeight(dto.getTargetWeight());
        }

        if (dto.getTargetCalories() != null) {
            goal.setTargetCalories(dto.getTargetCalories());
        }

        if (dto.getTargetDate() != null) {
            goal.setTargetDate(dto.getTargetDate());
        }

        return goal;
    }

    public GoalSummaryDto toSummary(Goal goal) {
        return GoalSummaryDto.builder()
                .id(goal.getId())
                .status(goal.getStatus().toString())
                .targetCalories(goal.getTargetCalories())
                .progressPercentage(null) // logic needed
                .daysRemaining(ChronoUnit.DAYS.between(LocalDate.now(), goal.getTargetDate()))
                .build();

    }

    public ReadGoalDto toDto (Goal goal) {
        return ReadGoalDto.builder()
                .id(goal.getId())
                .goalType(goal.getGoalType().toString())
                .description(goal.getDescription())
                .status(goal.getStatus().toString())
                .startDate(goal.getStartDate())
                .targetDate(goal.getTargetDate())
                .targetWeight(goal.getTargetWeight())
                .targetBodyFat(goal.getTargetBodyFat())
                .targetCalories(goal.getTargetCalories())
                .totalDietPlans((long) goal.getDietPlans().size())
                .completedDietPlans(null)  // logic needed here
                .dietPlanIds(goal.getDietPlans().stream().map(dp -> dp.getId()).toList())
                .build();
    }

}
