package com.abdelaziz26.metriplate.entities.user;

import com.abdelaziz26.metriplate.enums.ActivityLevel;
import com.abdelaziz26.metriplate.enums.DietGoal;
import com.abdelaziz26.metriplate.enums.DietType;
import com.abdelaziz26.metriplate.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "health_metrics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HealthMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // ── Basic Metrics ──────────────────────────
    private Integer age;
    private Double weightKg;
    private Double heightCm;

    // Optional body composition fields — provided if user has a smart scale or DEXA scan.
    // Both are nullable; the system degrades gracefully when absent.
    private Double muscleMassKg;       // absolute muscle mass in kg
    private Double fatPercentage;      // body fat as a percentage (e.g. 22.5 means 22.5%)

    @Enumerated(EnumType.STRING)
    private Gender gender;                // MALE, FEMALE, OTHER

    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;  // SEDENTARY, LIGHTLY_ACTIVE,
                                          // MODERATELY_ACTIVE, VERY_ACTIVE

    // ── Medical / Conditions ───────────────────
    private String medicalNotes;          // free-text; anything the user wants to share

    // ── Diet Preferences ──────────────────────
    @Enumerated(EnumType.STRING)
    private DietType dietType;            // NONE, VEGETARIAN, VEGAN, KETO,
                                          // PALEO, MEDITERRANEAN, etc.

    @Enumerated(EnumType.STRING)
    private DietGoal dietGoal;            // LOSE_WEIGHT, GAIN_MUSCLE,
                                          // MAINTAIN_WEIGHT, IMPROVE_HEALTH

    // Target the user sets themselves
    private Double targetWeightKg;
    private Integer dailyCalorieTarget;   // optional override; else system calculates

    private Double bmi;
    private String bmiCategory;

    private Integer tdee;

    // override or calculated
    private Double fatMass;
    private Double leanMass;

    private String bodyFatCategory;




}