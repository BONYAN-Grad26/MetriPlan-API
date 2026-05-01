package com.abdelaziz26.metriplate.dtos.metrics;

import com.abdelaziz26.metriplate.enums.ActivityLevel;
import com.abdelaziz26.metriplate.enums.DietGoal;
import com.abdelaziz26.metriplate.enums.DietType;
import com.abdelaziz26.metriplate.enums.Gender;
import lombok.*;

import java.util.Set;

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
