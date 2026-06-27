package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.store.CartItem;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<@NotNull CartItem, @NotNull Long> {

    @EntityGraph(attributePaths = {
            "ingredient"
    })
    List<CartItem> findByUser_Id(Long userId);
}
