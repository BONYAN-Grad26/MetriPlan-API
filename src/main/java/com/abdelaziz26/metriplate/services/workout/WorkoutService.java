package com.abdelaziz26.metriplate.services.workout;

import com.abdelaziz26.metriplate.dtos.workout.WorkoutDayDto;
import com.abdelaziz26.metriplate.dtos.workout.WorkoutPlanResponseDto;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;

import java.util.List;

public interface WorkoutService {
    Result<WorkoutPlanResponseDto, Error> generateWeeklyWorkoutPlan() throws Exception;
    Result<WorkoutPlanResponseDto, Error> getById(Long id);
    Result<List<WorkoutPlanResponseDto>, Error> getByUserId(Long id);
    Result<WorkoutDayDto, Error> getTodayPlan() ;
    Result<String, Error> deletePlan(Long id) ;
    Result<WorkoutPlanResponseDto, Error> getMyWeeklyPlan();
}
