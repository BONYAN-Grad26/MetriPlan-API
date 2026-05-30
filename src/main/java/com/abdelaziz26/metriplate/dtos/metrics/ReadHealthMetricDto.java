package com.abdelaziz26.metriplate.dtos.metrics;

import com.abdelaziz26.metriplate.enums.user.ActivityLevel;
import com.abdelaziz26.metriplate.enums.diet.DietGoal;
import com.abdelaziz26.metriplate.enums.diet.DietType;
import com.abdelaziz26.metriplate.enums.user.Gender;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ReadHealthMetricDto {

    private Long id;
    
    // ── Basic Metrics ──────────────────────────
    private Integer age;
    private Double weightKg;
    private Double heightCm;
    private Double muscleMassKg;
    private Double fatPercentage;
    
    private Gender gender;
    private ActivityLevel activityLevel;
    
    // ── Medical / Conditions ───────────────────
    private String medicalNotes;
    
    // ── Diet Preferences ──────────────────────
    private DietType dietType;
    private DietGoal dietGoal;

    // ── Calculated Metrics ──────────────────────
    private Double bmi;
    private String bmiCategory;
    private Integer tdee;
    private Double fatMass;
    private Double leanMass;
    private String bodyFatCategory;

    // ── Goals ──────────────────────────────────
    private Double targetWeightKg;
    private Integer dailyCalorieTarget;
    
    // ── Relationships ──────────────────────────
    //private Set<ReadAllergyDto> allergies;
    
    // ── Timestamps ─────────────────────────────
    //private LocalDateTime updatedAt;
}
