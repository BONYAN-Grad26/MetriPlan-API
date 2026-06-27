package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.store.Order;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<@NotNull Order, @NotNull Long> {
    List<Order> findByUser_Id(Long userId);
}
