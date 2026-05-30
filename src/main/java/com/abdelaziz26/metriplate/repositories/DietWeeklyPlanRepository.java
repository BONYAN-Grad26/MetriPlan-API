package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.diet.WeeklyPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DietWeeklyPlanRepository extends JpaRepository<WeeklyPlan, Long> {
    List<WeeklyPlan> findByUser_IdOrderByWeekNumberDesc(Long userId);
}
