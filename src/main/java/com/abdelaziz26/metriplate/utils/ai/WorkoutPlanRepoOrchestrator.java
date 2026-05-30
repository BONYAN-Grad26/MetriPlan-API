package com.abdelaziz26.metriplate.utils.ai;

import com.abdelaziz26.metriplate.dtos.workout.WorkoutPlanResponseDto;
import com.abdelaziz26.metriplate.entities.user.User;
import com.abdelaziz26.metriplate.entities.workout.WorkoutPlan;
import com.abdelaziz26.metriplate.mappers.WorkoutPlanMapper;
import com.abdelaziz26.metriplate.repositories.WorkoutDayPlanRepository;
import com.abdelaziz26.metriplate.repositories.WorkoutWeeklyPlanRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkoutPlanRepoOrchestrator {

    WorkoutPlanMapper           workoutPlanMapper;
    WorkoutWeeklyPlanRepository workoutWeeklyPlanRepository;

    public WorkoutPlan saveWorkoutPlan(WorkoutPlanResponseDto workoutPlanDto, User user) {
        WorkoutPlan entity = workoutPlanMapper.toEntity(workoutPlanDto, user);
        return workoutWeeklyPlanRepository.save(entity);
    }
}
