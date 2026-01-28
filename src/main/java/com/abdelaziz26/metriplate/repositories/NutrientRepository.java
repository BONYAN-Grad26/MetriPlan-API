package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.enums.NutrientType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.abdelaziz26.metriplate.entities.Nutrient;

import java.util.Optional;

public interface NutrientRepository extends JpaRepository<Nutrient, Long> {
    boolean existsByNutrientTypeOrName(NutrientType type, String name);

    Optional<Nutrient> findByNutrientType(NutrientType type);
}
