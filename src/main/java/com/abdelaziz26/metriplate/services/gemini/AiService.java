package com.abdelaziz26.metriplate.services.gemini;

import com.abdelaziz26.metriplate.entities.DietPlan;
import com.abdelaziz26.metriplate.exceptions.PlanGenerationException;

public interface AiService {
    DietPlan generateAndSaveDietPlan() throws PlanGenerationException;
}
