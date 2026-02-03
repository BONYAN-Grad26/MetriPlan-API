package com.abdelaziz26.metriplate.services.ai;

import com.abdelaziz26.metriplate.dtos.plan.DietPlanDTO;
import com.abdelaziz26.metriplate.exceptions.PlanGenerationException;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;

public interface AiService {
    Result<DietPlanDTO, Error> generateAndSaveDietPlan() throws PlanGenerationException;
}
