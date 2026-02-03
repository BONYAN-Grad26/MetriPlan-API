package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.DietPlan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DietPlanRepository extends CrudRepository<DietPlan, Long> {
    List<DietPlan> findByUser_IdOrderByStartDateDesc(Long userId);
    List<DietPlan> findByGoalId(Long goalId);

    @Query("SELECT dp FROM DietPlan dp " +
            "LEFT JOIN FETCH dp.weeklyPlans wp " +
            "LEFT JOIN FETCH wp.dailyPlans dpn " +
            "LEFT JOIN FETCH dpn.meals dpm " +
            "LEFT JOIN FETCH dpm.meal m " +
            "LEFT JOIN FETCH m.ingredients mi " +
            "LEFT JOIN FETCH mi.ingredient " +
            "WHERE dp.id = :id")
    Optional<DietPlan> findByIdWithDetails(@Param("id") Long id);


}
