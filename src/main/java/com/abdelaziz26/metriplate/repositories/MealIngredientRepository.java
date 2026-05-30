package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.diet.MealIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealIngredientRepository extends JpaRepository<MealIngredient, Long> {
}
