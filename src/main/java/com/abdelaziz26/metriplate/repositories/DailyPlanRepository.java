package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.DailyPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyPlanRepository extends JpaRepository<DailyPlan, Long> {

    List<DailyPlan> findByWeeklyPlanIdOrderByDate(Long weeklyPlanId);

    // handy for "today's plan" lookups later
    Optional<DailyPlan> findByWeeklyPlan_DietPlan_User_IdAndDate(Long userId, LocalDate date);
}
