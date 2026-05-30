package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.workout.Exercise;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<@NotNull Exercise, @NotNull Long> {
}
