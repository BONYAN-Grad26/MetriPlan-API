package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.Allergy;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AllergyRepository extends JpaRepository<@NotNull Allergy, @NotNull Long> {
    List<Allergy> findByUser_Id(Long userId);

    boolean existsByIdAndUser_Id(Long id, Long userId);

    List<Allergy> findByUserId(Long userId);
}
