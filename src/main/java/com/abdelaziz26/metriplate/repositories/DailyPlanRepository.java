package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.diet.DailyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyPlanRepository extends JpaRepository<DailyPlan, Long> {

    List<DailyPlan> findByWeeklyPlanIdOrderByDate(Long weeklyPlanId);

    // handy for "today's plan" lookups later
    @Query("SELECT d FROM DailyPlan d WHERE d.weeklyPlan.user.id = :userId AND d.date = :date")
    Optional<DailyPlan> findByWeeklyPlan_User_IdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
}
