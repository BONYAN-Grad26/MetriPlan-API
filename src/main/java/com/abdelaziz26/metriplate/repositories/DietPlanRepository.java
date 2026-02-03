package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.DietPlan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DietPlanRepository extends CrudRepository<DietPlan, Long> {
    List<DietPlan> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<DietPlan> findByGoalId(Long goalId);
}
