package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.diet.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<@NotNull Ingredient, @NotNull Long>, JpaSpecificationExecutor<@NotNull Ingredient> {

    @EntityGraph(attributePaths = {"dietaryTags"})
    @NotNull Optional<Ingredient> findByNameLike(@NotNull String name);

    @EntityGraph(attributePaths = {"dietaryTags"})
    @NotNull Optional<Ingredient> findById(Long id);

    Boolean existsByName(@NotNull String name);

    @NotNull Page<@NotNull Ingredient> findAll(@NotNull Pageable pageable);

    @Query("SELECT DISTINCT i FROM Ingredient i LEFT JOIN FETCH i.dietaryTags")
    List<Ingredient> findAllWithDietaryTags();

    List<Ingredient> findByNameIn(List<String> names);
}
