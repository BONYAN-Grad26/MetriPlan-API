package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.diet.WeeklyPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeeklyPlanRepository extends JpaRepository<WeeklyPlan, Long> {
    List<WeeklyPlan> findByDietPlanIdOrderByWeekNumber(Long dietPlanId);
}
