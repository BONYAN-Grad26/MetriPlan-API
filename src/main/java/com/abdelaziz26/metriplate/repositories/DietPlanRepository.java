package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.diet.DietPlan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DietPlanRepository extends CrudRepository<DietPlan, Long> {
    List<DietPlan> findByUser_IdOrderByStartDateDesc(Long userId);
    List<DietPlan> findByGoalId(Long goalId);

}
