package com.abdelaziz26.metriplate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abdelaziz26.metriplate.entities.Goal;

import java.util.List;
import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findAllByUser_Id(Long userId);
    boolean existsByIdAndUser_Id(Long id, Long userId);
}
