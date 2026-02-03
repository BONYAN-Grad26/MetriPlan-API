package com.abdelaziz26.metriplate.responses.Gemini;

import com.abdelaziz26.metriplate.dtos.plan.DietPlanDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class GeminiDietPlanResponse {
    private String status;
    private String reason;
    private DietPlanDTO dietPlan;
}
