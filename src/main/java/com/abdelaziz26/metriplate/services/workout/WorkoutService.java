package com.abdelaziz26.metriplate.services.workout;

import com.abdelaziz26.metriplate.dtos.workout.WorkoutPlanResponseDto;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;

public interface WorkoutService {
    Result<WorkoutPlanResponseDto, Error> generateWeeklyWorkoutPlan();
}
