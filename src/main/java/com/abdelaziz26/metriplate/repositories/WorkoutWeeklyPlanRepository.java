package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.workout.WorkoutPlan;
import jakarta.persistence.Entity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkoutWeeklyPlanRepository extends JpaRepository<@NotNull WorkoutPlan, @NotNull Long> {
    List<WorkoutPlan> findByUser_Id(@NotNull Long id);
    List<WorkoutPlan> findByUser_IdOrderByIdDesc(@NotNull Long id);
    Boolean existsByIdAndUser_Id(@NotNull Long id, @NotNull Long userId);

    boolean existsByUser_Id(Long userId);

    @Override
    @EntityGraph(attributePaths = {"days", "days.exercises"})
    Optional<@NotNull WorkoutPlan> findById(@NotNull Long aLong);

}
