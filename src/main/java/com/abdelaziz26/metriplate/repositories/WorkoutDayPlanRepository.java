package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.workout.WorkoutDay;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WorkoutDayPlanRepository extends JpaRepository<@NotNull WorkoutDay, @NotNull Long> {

    @Query("SELECT wd FROM WorkoutDay wd " +
            "WHERE wd.plan.user.id = :userId " +
            "AND LOWER(wd.dayName) = LOWER(:dayName)")
    Optional<WorkoutDay> findByUserIdAndDay(@Param("userId") Long userId, @Param("dayName") String dayName);
}
