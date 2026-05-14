package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.diet.Meal;
import com.abdelaziz26.metriplate.enums.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {

    @Query("""
            SELECT DISTINCT m FROM Meal m
            JOIN FETCH m.ingredients mi
            JOIN FETCH mi.ingredient
            WHERE m.id = :mealId
            """)
    java.util.Optional<Meal> findByIdWithIngredients(@Param("mealId") Long mealId);

    List<Meal> findByMealType(MealType mealType);
}
