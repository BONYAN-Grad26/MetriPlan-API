package com.abdelaziz26.metriplate.services.workout;

import com.abdelaziz26.metriplate.dtos.workout.WorkoutPlanResponseDto;
import com.abdelaziz26.metriplate.entities.user.HealthMetrics;
import com.abdelaziz26.metriplate.entities.user.User;
import com.abdelaziz26.metriplate.exceptions.PlanGenerationException;
import com.abdelaziz26.metriplate.mappers.WorkoutPlanMapper;
import com.abdelaziz26.metriplate.repositories.MetricsRepository;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Errors;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.security.SecurityContextService;
import com.abdelaziz26.metriplate.services.ai.LlmClient;
import com.abdelaziz26.metriplate.utils.ai.WorkoutJsonParser;
import com.abdelaziz26.metriplate.utils.ai.WorkoutPlanRepoOrchestrator;
import com.abdelaziz26.metriplate.utils.ai.WorkoutPromptBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkoutServiceImpl implements WorkoutService {

    WorkoutPromptBuilder        promptBuilder;
    LlmClient                   llmClient;
    MetricsRepository           metricsRepository;
    WorkoutJsonParser           workoutJsonParser;
    SecurityContextService      contextService;
    WorkoutPlanRepoOrchestrator repoOrchestrator;
    WorkoutPlanMapper           workoutPlanMapper;

    @Override
    public Result<WorkoutPlanResponseDto, Error> generateWeeklyWorkoutPlan() {
        try {

            User user = contextService.getCurrentUser().orElse(null);
            if (user == null) {
                return Result.CreateErrorResult(Errors.UnauthorizedErr("User not authenticated"));
            }

            HealthMetrics metrics = metricsRepository.findByUser_Id(user.getId())
                    .orElseThrow(() -> new PlanGenerationException("Health metrics not found for user: " + user.getId()));

            String prompt = promptBuilder.buildPrompt(metrics);

            String llmResponse = llmClient.generateContent(prompt);

            WorkoutPlanResponseDto workoutPlan = workoutJsonParser.parseWorkoutPlanResponse(llmResponse);

            return Result.CreateSuccessResult(workoutPlanMapper.toDto(repoOrchestrator.saveWorkoutPlan(workoutPlan, user)));

        } catch (IOException e) {
            return Result.CreateErrorResult(Errors.InternalServerErr("Failed to parse LLM response: " + e.getMessage()));
        } catch (Exception e) {
            return Result.CreateErrorResult(Errors.InternalServerErr("Workout plan generation failed: " + e.getMessage()));
        }
    }
}
