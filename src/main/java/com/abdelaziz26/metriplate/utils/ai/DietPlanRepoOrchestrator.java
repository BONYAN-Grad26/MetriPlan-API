package com.abdelaziz26.metriplate.utils.ai;

import com.abdelaziz26.metriplate.dtos.plan.DayDTO;
import com.abdelaziz26.metriplate.dtos.plan.MealDTO;
import com.abdelaziz26.metriplate.dtos.plan.WeekDTO;
import com.abdelaziz26.metriplate.entities.diet.*;
import com.abdelaziz26.metriplate.entities.user.User;
import com.abdelaziz26.metriplate.mappers.WeeklyPlanMapper;
import com.abdelaziz26.metriplate.repositories.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DietPlanRepoOrchestrator {
    private final DietWeeklyPlanRepository weeklyPlanRepository;
    private final WeeklyPlanMapper weeklyPlanMapper;

    @Transactional
    public WeeklyPlan saveWeeklyPlan(WeekDTO weekDto, User user) {

        if (weekDto == null) {
            throw new IllegalArgumentException("WeekDTO cannot be null");
        }

        WeeklyPlan weeklyPlan = weeklyPlanMapper.toEntity(weekDto);
        weeklyPlan.setUser(user);

        WeeklyPlan saved = weeklyPlanRepository.save(weeklyPlan);

        log.info(
                "Saved WeeklyPlan ID={} with {} daily plans",
                saved.getId(),
                saved.getDailyPlans() != null
                        ? saved.getDailyPlans().size()
                        : 0
        );

        return saved;
    }
}
