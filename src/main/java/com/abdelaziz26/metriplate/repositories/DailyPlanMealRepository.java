package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.DailyPlanMeal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyPlanMealRepository extends JpaRepository<DailyPlanMeal, Long> {
    List<DailyPlanMeal> findByDailyPlanIdOrderByMealOrder(Long dailyPlanId);
}
