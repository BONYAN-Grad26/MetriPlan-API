package com.abdelaziz26.metriplate.services.plan;

import com.abdelaziz26.metriplate.dtos.plan.DietPlanDTO;
import com.abdelaziz26.metriplate.dtos.plan.DietPlanSimpleResponseDto;
import com.abdelaziz26.metriplate.entities.DietPlan;
import com.abdelaziz26.metriplate.entities.Goal;
import com.abdelaziz26.metriplate.entities.User;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;

import java.util.List;

public interface PlanService {
    DietPlan persistPlans(DietPlanDTO planDto, User user, Goal goal);
    Result<DietPlanDTO, Error> getById(Long id);
    Result<List<DietPlanSimpleResponseDto>, Error> getByUserId();

}
