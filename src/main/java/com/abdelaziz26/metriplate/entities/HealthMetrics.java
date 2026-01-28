package com.abdelaziz26.metriplate.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "health_metrics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate recordedAt;

    private Double bodyFatPercentage;
    private Double muscleMass;
    private Double bmi;
    private Double waistCircumference;
    private Double hipCircumference;


    @Column(length = 1000)
    private String notes;
}