package com.abdelaziz26.metriplate.dtos.metrics;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class NutritionCalcResult {

    private Double bmi;
    private String bmiCategory;

    private Integer tdee;
    private Integer calorieTarget;

    private Double fatMass;
    private Double leanMass;
    private String bodyFatCategory;
}
