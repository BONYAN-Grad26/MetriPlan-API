package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.workout.WorkoutDay;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutDayPlanRepository extends JpaRepository<@NotNull WorkoutDay, @NotNull Long> {
}
