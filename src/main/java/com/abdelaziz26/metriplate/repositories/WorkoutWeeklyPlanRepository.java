package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.workout.WorkoutPlan;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutWeeklyPlanRepository extends JpaRepository<@NotNull WorkoutPlan, @NotNull Long> {
    List<WorkoutPlan> findByUser_Id(@NotNull Long id);
}
