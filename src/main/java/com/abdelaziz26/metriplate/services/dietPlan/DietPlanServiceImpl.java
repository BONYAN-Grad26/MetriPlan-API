package com.abdelaziz26.metriplate.services.dietPlan;

import com.abdelaziz26.metriplate.dtos.plan.WeekDTO;
import com.abdelaziz26.metriplate.entities.diet.DailyPlan;
import com.abdelaziz26.metriplate.entities.diet.Ingredient;
import com.abdelaziz26.metriplate.entities.user.Allergy;
import com.abdelaziz26.metriplate.entities.user.HealthMetrics;
import com.abdelaziz26.metriplate.entities.user.User;
import com.abdelaziz26.metriplate.entities.workout.WorkoutPlan;
import com.abdelaziz26.metriplate.exceptions.PlanGenerationException;
import com.abdelaziz26.metriplate.repositories.AllergyRepository;
import com.abdelaziz26.metriplate.repositories.IngredientRepository;
import com.abdelaziz26.metriplate.repositories.MetricsRepository;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Errors;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.security.SecurityContextService;
import com.abdelaziz26.metriplate.services.ai.LlmClient;
import com.abdelaziz26.metriplate.utils.ai.DietJsonParser;
import com.abdelaziz26.metriplate.utils.ai.DietPlanRepoOrchestrator;
import com.abdelaziz26.metriplate.utils.ai.WeeklyDietPlanPromptBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.abdelaziz26.metriplate.mappers.WeeklyPlanMapper;
import com.abdelaziz26.metriplate.entities.diet.WeeklyPlan;
import com.abdelaziz26.metriplate.repositories.DietWeeklyPlanRepository;
import com.abdelaziz26.metriplate.repositories.DailyPlanRepository;
import com.abdelaziz26.metriplate.dtos.plan.DayDTO;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DietPlanServiceImpl implements DietPlanService{

    WeeklyDietPlanPromptBuilder     promptBuilder;
    LlmClient                       llmClient;
    MetricsRepository               metricsRepository;
    IngredientRepository            ingredientRepository;
    AllergyRepository               allergyRepository;
    DietJsonParser                  dietJsonParser;
    SecurityContextService          contextService;
    DietWeeklyPlanRepository        weeklyPlanRepository;
    WeeklyPlanMapper                weeklyPlanMapper;
    DailyPlanRepository             dailyPlanRepository;
    DietPlanRepoOrchestrator        repoOrchestrator;

    //@Transactional(readOnly = true)
    public Result<WeekDTO, Error> generateWeeklyPlan(LocalDate startDate, int weekNumber) throws Exception {
        User user = contextService.getCurrentUser().orElse(null);

        boolean hasOngoingPlans = weeklyPlanRepository.existsByUser_IdAndStartDateBeforeAndEndDateAfter(user.getId(),
                LocalDate.now(), LocalDate.now());

        if(hasOngoingPlans) {
            return Result.CreateErrorResult(Errors.BadRequestErr("You have Ongoing Plan"));
        }

        if(user == null) {
            return Result.CreateErrorResult(Errors.UnauthorizedErr("User not authenticated"));
        }

        HealthMetrics metrics = metricsRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new PlanGenerationException("Health metrics not found for user: " + user.getId()));

        List<Ingredient> availableIngredients = ingredientRepository.findAll();
        List<Allergy> userAllergies = allergyRepository.findByUserId(user.getId());

        String prompt = promptBuilder.buildPrompt(metrics, availableIngredients, userAllergies, startDate, weekNumber);

        String llmResponse = llmClient.generateContent(prompt);

        log.info("LLM Response: {}", llmResponse);

        WeekDTO weekDto = dietJsonParser.parseWeeklyPlanResponse(llmResponse, startDate, weekNumber);

        log.info("Parsed WeekDTO: {}", weekDto);

        WeeklyPlan wp = repoOrchestrator.saveWeeklyPlan(weekDto, user);

        return Result.CreateSuccessResult(weeklyPlanMapper.toDto(wp));
    }

    public Result<List<WeekDTO>, Error> getWeeklyPlansByUserId() {
        User user = contextService.getCurrentUser().orElse(null);
        if (user == null) {
            return Result.CreateErrorResult(Errors.UnauthorizedErr("User not authenticated"));
        }

        List<WeeklyPlan> plans = weeklyPlanRepository.findByUser_IdOrderByWeekNumberDesc(user.getId());
        
        if (plans == null || plans.isEmpty()) {
            return Result.CreateSuccessResult(null);
        }

        return Result.CreateSuccessResult(plans.stream().map(weeklyPlanMapper::toDto).toList());
    }

    public Result<WeekDTO, Error> getWeeklyPlanById(Long planId) {
        Optional<WeeklyPlan> optional = weeklyPlanRepository.findById(planId);
        if (optional.isEmpty()) {
            return Result.CreateErrorResult(Errors.NotFoundErr("Weekly plan not found"));
        }
        WeekDTO dto = weeklyPlanMapper.toDto(optional.get());
        return Result.CreateSuccessResult(dto);
    }

    public Result<WeekDTO, Error> getDailyPlanById(Long planId) {
        Optional<WeeklyPlan> weeklyPlan = weeklyPlanRepository.findAll()
                .stream()
                .filter(wp -> wp.getDailyPlans() != null && wp.getDailyPlans().stream().anyMatch(dp -> dp.getId() != null && dp.getId().equals(planId)))
                .findFirst();

        if (weeklyPlan.isEmpty()) {
            return Result.CreateErrorResult(Errors.NotFoundErr("Daily plan not found"));
        }

        WeekDTO dto = weeklyPlanMapper.toDto(weeklyPlan.get());
        return Result.CreateSuccessResult(dto);
    }

    public Result<DayDTO, Error> getTodayPlan() {
        User user = contextService.getCurrentUser().orElse(null);
        if (user == null) {
            return Result.CreateErrorResult(Errors.UnauthorizedErr("User not authenticated"));
        }

        LocalDate today = LocalDate.now();

        Optional<DailyPlan> dailyOpt = dailyPlanRepository
                .findByWeeklyPlan_User_IdAndDate(user.getId(), today);

        if (dailyOpt.isEmpty()) {
            return Result.CreateErrorResult(Errors.NotFoundErr("Today's plan not found"));
        }

        DayDTO dayDto = weeklyPlanMapper.toDayDto(dailyOpt.get());
        return Result.CreateSuccessResult(dayDto);
    }

    public Result<WeekDTO, Error> updateWeeklyPlan(Long planId, WeekDTO updatedPlan) {
        Optional<WeeklyPlan> optional = weeklyPlanRepository.findById(planId);
        if (optional.isEmpty()) {
            return Result.CreateErrorResult(Errors.NotFoundErr("Weekly plan not found"));
        }

        WeeklyPlan existing = optional.get();

        WeeklyPlan mapped = weeklyPlanMapper.toEntity(updatedPlan);
        mapped.setId(existing.getId());
        mapped.setUser(existing.getUser());
        //mapped.setDietPlan(existing.getDietPlan());

        WeeklyPlan saved = weeklyPlanRepository.save(mapped);
        WeekDTO dto = weeklyPlanMapper.toDto(saved);
        return Result.CreateSuccessResult(dto);
    }

    public Result<String, Error> deleteWeeklyPlan(Long planId) {
        Optional<WeeklyPlan> optional = weeklyPlanRepository.findById(planId);
        if (optional.isEmpty()) {
            return Result.CreateErrorResult(Errors.NotFoundErr("Weekly plan not found"));
        }
        weeklyPlanRepository.delete(optional.get());
        return Result.CreateSuccessResult("Deleted");
    }

}

