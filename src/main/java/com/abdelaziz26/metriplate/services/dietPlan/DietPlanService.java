package com.abdelaziz26.metriplate.services.dietPlan;

import com.abdelaziz26.metriplate.dtos.plan.DayDTO;
import com.abdelaziz26.metriplate.dtos.plan.DietPlanDTO;
import com.abdelaziz26.metriplate.dtos.plan.DietPlanSimpleResponseDto;
import com.abdelaziz26.metriplate.dtos.plan.WeekDTO;
import com.abdelaziz26.metriplate.entities.diet.DietPlan;
import com.abdelaziz26.metriplate.entities.user.Goal;
import com.abdelaziz26.metriplate.entities.user.User;
import com.abdelaziz26.metriplate.exceptions.PlanGenerationException;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface DietPlanService {
    Result<WeekDTO, Error> generateWeeklyPlan(LocalDate startDate, int weekNumber) throws IOException, PlanGenerationException, Exception;
    Result<List<WeekDTO>, Error> getWeeklyPlansByUserId();
    Result<WeekDTO, Error> getWeeklyPlanById(Long planId);
    Result<WeekDTO, Error> getDailyPlanById(Long planId);
    Result<DayDTO, Error> getTodayPlan();
    Result<WeekDTO, Error> updateWeeklyPlan(Long planId, WeekDTO updatedPlan);
    Result<String, Error> deleteWeeklyPlan(Long planId);

}
