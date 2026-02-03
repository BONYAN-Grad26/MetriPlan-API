package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.Allergy;
import jakarta.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AllergyRepository extends JpaRepository<@NotNull Allergy, @NotNull Long> {
    List<Allergy> findByUser_Id(Long userId);
    List<Allergy> findByNutrient_Id(Long nutrientId);

    boolean existsByIdAndUser_Id(Long id, Long userId);
    boolean existsByUser_IdAndNutrient_Id(Long userId, Long nutrientId);

    @Query("SELECT a FROM Allergy a JOIN FETCH a.nutrient WHERE a.user.id = :userId")
    List<Allergy> findByUserId(@Param("userId") Long userId);
}
