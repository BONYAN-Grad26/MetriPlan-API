package com.abdelaziz26.metriplate.services.plan;

import com.abdelaziz26.metriplate.dtos.plan.*;
import com.abdelaziz26.metriplate.entities.*;
import com.abdelaziz26.metriplate.repositories.*;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Errors;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.security.SecurityContextService;
import com.abdelaziz26.metriplate.services.meal.MealService;
import com.abdelaziz26.metriplate.utils.MacroCalculator;
import com.abdelaziz26.metriplate.utils.mappers.DailyPlanMapper;
import com.abdelaziz26.metriplate.utils.mappers.DietPlanMapper;
import com.abdelaziz26.metriplate.utils.mappers.MealMapper;
import com.abdelaziz26.metriplate.utils.mappers.WeeklyPlanMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final DietPlanRepository dietPlanRepo;
    private final WeeklyPlanRepository weeklyPlanRepo;
    private final DailyPlanRepository dailyPlanRepo;
    private final MealRepository mealRepo;
    private final IngredientRepository ingredientRepo;
    private final DailyPlanMealRepository dailyPlanMealRepo;

    private final DietPlanMapper dietPlanMapper;
    private final WeeklyPlanMapper weeklyPlanMapper;
    private final MealMapper mealMapper;
    private final DailyPlanMapper dailyPlanMapper;

    private final MealService mealService;

    private final SecurityContextService contextService;

    Map<String, Ingredient> ingredientMap = new HashMap<>();  // need to be filled


    @Override
    public Result<DietPlanDTO, Error> getById(Long id) {
        return dietPlanRepo.findByIdWithDetails(id).map(p -> Result.CreateSuccessResult(dietPlanMapper.toDto(p)))
                .orElse(Result.CreateErrorResult(Errors.NotFoundErr("No Plan Found! ")));
    }

    public Result<List<DietPlanSimpleResponseDto>, Error> getByUserId() {
        User user = contextService.getCurrentUser().orElse(null);

        if (user == null) {
            return Result.CreateErrorResult(Errors.UnauthorizedErr("You are not authorized, Login first."));
        }

        List<DietPlan> plans = dietPlanRepo.findByUser_IdOrderByStartDateDesc(user.getId());

        return Result.CreateSuccessResult(plans.stream().map(dietPlanMapper::toSimpleDto).collect(Collectors.toList()));
    }


    // ----_____________------ need to be refactored -> use cascade

    @Transactional
    public DietPlan persistPlans(DietPlanDTO planDto, User user, Goal goal) {

        DietPlan dietPlan = dietPlanMapper.toEntity(planDto, user, goal);
        DietPlan savedPlan = dietPlanRepo.save(dietPlan);

        for(WeekDTO wpDto : planDto.getWeeks()) {
            WeeklyPlan wp = weeklyPlanMapper.toEntity(wpDto, savedPlan);
            WeeklyPlan savedWeek = weeklyPlanRepo.save(wp);

            for(DayDTO dpDto : wpDto.getDays()) {
                DailyPlan dp = dailyPlanMapper.toEntity(dpDto, savedWeek);
                DailyPlan savedDp = dailyPlanRepo.save(dp);

                int mealOrder = 1;
                for (MealDTO mealDto : dpDto.getMeals()) {
                    Meal meal = mealMapper.toEntity(mealDto);

                    List<MealIngredient> mealIngredients = mealService.createMealIngredients(mealDto, ingredientMap);
                    meal.setIngredients(mealIngredients);

                    Meal savedMeal = mealRepo.save(meal);

                    DailyPlanMeal link = new DailyPlanMeal();
                    link.setDailyPlan(savedDp);
                    link.setMeal(savedMeal);
                    link.setMealOrder(mealOrder);
                    link.setIsCustomized(false);
                    dailyPlanMealRepo.save(link);

                    mealOrder++;
                }
            }
        }

        return savedPlan;
    }

}
