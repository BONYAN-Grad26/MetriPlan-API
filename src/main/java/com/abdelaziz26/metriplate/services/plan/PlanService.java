package com.abdelaziz26.metriplate.services.plan;

import com.abdelaziz26.metriplate.dtos.plan.DietPlanDTO;
import com.abdelaziz26.metriplate.entities.DietPlan;
import com.abdelaziz26.metriplate.entities.Goal;
import com.abdelaziz26.metriplate.entities.User;

public interface PlanService {
    DietPlan persistPlans(DietPlanDTO planDto, User user, Goal goal);
}
