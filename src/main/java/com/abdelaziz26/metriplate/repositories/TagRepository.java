package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.DietaryTag;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<@NotNull DietaryTag, @NotNull Long> {
    Optional<DietaryTag> findByName(String name);
    @NotNull Page<@NotNull DietaryTag> findAll(@NotNull Pageable pageable);

    boolean existsByName(@NotNull String name);
}
