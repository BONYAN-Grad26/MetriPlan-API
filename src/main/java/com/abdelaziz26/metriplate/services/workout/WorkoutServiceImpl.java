package com.abdelaziz26.metriplate.services.workout;

import com.abdelaziz26.metriplate.dtos.workout.WorkoutDayDto;
import com.abdelaziz26.metriplate.dtos.workout.WorkoutPlanResponseDto;
import com.abdelaziz26.metriplate.entities.user.HealthMetrics;
import com.abdelaziz26.metriplate.entities.user.User;
import com.abdelaziz26.metriplate.exceptions.PlanGenerationException;
import com.abdelaziz26.metriplate.mappers.WorkoutPlanMapper;
import com.abdelaziz26.metriplate.repositories.MetricsRepository;
import com.abdelaziz26.metriplate.repositories.WorkoutDayPlanRepository;
import com.abdelaziz26.metriplate.repositories.WorkoutWeeklyPlanRepository;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service("workoutService")
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
    WorkoutWeeklyPlanRepository workoutWeeklyPlanRepository;
    WorkoutDayPlanRepository    workoutDayPlanRepository;

    @Override
    public Result<WorkoutPlanResponseDto, Error> generateWeeklyWorkoutPlan() throws Exception {
        User user = contextService.getCurrentUser().orElse(null);

        if (user == null) {
            return Result.CreateErrorResult(Errors.UnauthorizedErr("User not authenticated"));
        }

        boolean hasOngoing = workoutWeeklyPlanRepository.existsByUser_Id(user.getId());

        if(hasOngoing) {
            return Result.CreateErrorResult(Errors.BadRequestErr("You have ongoing plans"));
        }

        HealthMetrics metrics = metricsRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new PlanGenerationException("Health metrics not found for user: " + user.getId()));

        String prompt = promptBuilder.buildPrompt(metrics);

        String llmResponse = llmClient.generateContent(prompt);

        WorkoutPlanResponseDto workoutPlan = workoutJsonParser.parseWorkoutPlanResponse(llmResponse);

        return Result.CreateSuccessResult(workoutPlanMapper.toDto(repoOrchestrator.saveWorkoutPlan(workoutPlan, user)));

    }

    @PreAuthorize("@workoutService.isOwner(#id)")
    public Result<WorkoutPlanResponseDto, Error> getById(Long id) {

        return workoutWeeklyPlanRepository.findById(id)
                .map(wp -> Result.CreateSuccessResult(workoutPlanMapper.toDto(wp)))
                .orElse(Result.CreateErrorResult(Errors.NotFoundErr("Workout plan not found for id: " + id)));
    }

    public Result<List<WorkoutPlanResponseDto>, Error> getByUserId(Long userId) {
        User user = contextService.getCurrentUser().orElse(null);
        if (user == null) {
            return Result.CreateErrorResult(Errors.UnauthorizedErr("User not authenticated"));
        }

        return Result.CreateSuccessResult(workoutWeeklyPlanRepository.findByUser_Id(userId).stream().map(workoutPlanMapper::toDto).toList());
    }

    public Result<WorkoutPlanResponseDto, Error> getMyWeeklyPlan() {
        User user = contextService.getCurrentUser().orElse(null);
        if (user == null) {
            return Result.CreateErrorResult(Errors.UnauthorizedErr("User not authenticated"));
        }

        return workoutWeeklyPlanRepository.findFirstByUser_IdOrderByIdDesc(user.getId())
                .map(workoutPlanMapper::toDto)
                .map(Result::CreateSuccessResult)
                .orElseGet(() -> Result.CreateErrorResult(Errors.NotFoundErr("No workout plan found for this user")));
    }

    public Result<WorkoutDayDto, Error> getTodayPlan() {
        User user = contextService.getCurrentUser().orElse(null);
        if (user == null) {
            return Result.CreateErrorResult(Errors.UnauthorizedErr("User not authenticated"));
        }

        return workoutDayPlanRepository.findByUserIdAndDay(user.getId(), LocalDate.now().getDayOfWeek().name())
                .map(wp -> Result.CreateSuccessResult(workoutPlanMapper.toDayDto(wp)))
                .orElse(Result.CreateErrorResult(Errors.NotFoundErr("No workout plan found for today")));
    }

    @PreAuthorize("@workoutService.isOwner(#id)")
    public Result<String, Error> deletePlan(Long id) {
        workoutWeeklyPlanRepository.deleteById(id);
        return Result.CreateSuccessResult("Workout plan deleted successfully");
    }

    public Boolean isOwner(Long planId) {
        User user = contextService.getCurrentUser().orElse(null);
        if (user == null) {
            return false;
        }

        if (! workoutWeeklyPlanRepository.existsByIdAndUser_Id(planId, user.getId())){
            return false;
        }

        return true;
    }
}
