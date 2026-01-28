package com.abdelaziz26.metriplate.services.goal;

import com.abdelaziz26.metriplate.dtos.goal.CreateGoalDto;
import com.abdelaziz26.metriplate.dtos.goal.GoalSummaryDto;
import com.abdelaziz26.metriplate.dtos.goal.ReadGoalDto;
import com.abdelaziz26.metriplate.dtos.goal.UpdateGoalDto;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;

import java.util.List;

public interface GoalService {
    Result<List<ReadGoalDto>, Error> getGoalsByUserId();
    Result<ReadGoalDto, Error> AddGoal(CreateGoalDto createGoalDto);
    Result<ReadGoalDto, Error> UpdateGoal(Long id, UpdateGoalDto updateGoalDto);
    Result<String, Error> DeleteGoal(Long id);
    Result<List<GoalSummaryDto>, Error> getGoalsByUserId(Long userId);

}
