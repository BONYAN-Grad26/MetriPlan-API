package com.abdelaziz26.metriplate.dtos.metrics;

import com.abdelaziz26.metriplate.enums.user.ActivityLevel;
import com.abdelaziz26.metriplate.enums.diet.DietGoal;
import com.abdelaziz26.metriplate.enums.diet.DietType;
import com.abdelaziz26.metriplate.enums.user.Gender;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UpdateHealthMetricDto {

    private Integer age;
    private Double weightKg;
    private Double heightCm;
    private Double muscleMassKg;
    private Double fatPercentage;
    
    private Gender gender;
    private ActivityLevel activityLevel;
    
    private String medicalNotes;
    
    private DietType dietType;
    private DietGoal dietGoal;
    
    private Double targetWeightKg;
    private Integer dailyCalorieTarget;
    
    //private Set<Long> allergyIds;
}
