package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.user.HealthMetrics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MetricsRepository extends JpaRepository<HealthMetrics, Long> {
    Optional<HealthMetrics> findByUser_Id(Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);

    //Optional<HealthMetrics> findTopByUserIdOrderByRecordedAtDesc(Long userId);
}
