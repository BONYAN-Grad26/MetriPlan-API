package com.abdelaziz26.metriplate.dtos.metrics;

import com.abdelaziz26.metriplate.enums.*;
import com.abdelaziz26.metriplate.utils.annotations.ValidateEnumValue;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@NoArgsConstructor @AllArgsConstructor
@Getter
@Setter
public class CreateHealthMetricDto {

    @NotNull(message = "Age is required")
    @Min(value = 10, message = "Age must be at least 10")
    @Max(value = 120, message = "Age must be realistic")
    private Integer age;

    @NotNull(message = "Weight is required")
    @DecimalMin(value = "20.0", message = "Weight must be at least 20 kg")
    @DecimalMax(value = "500.0", message = "Weight must be realistic")
    private Double weightKg;

    @NotNull(message = "Height is required")
    @DecimalMin(value = "50.0", message = "Height must be at least 50 cm")
    @DecimalMax(value = "300.0", message = "Height must be realistic")
    private Double heightCm;


    @DecimalMin(value = "1.0", message = "Muscle mass must be at least 1 kg")
    @DecimalMax(value = "200.0", message = "Muscle mass must be realistic")
    private Double muscleMassKg;

    @DecimalMin(value = "1.0", message = "Fat percentage must be at least 1%")
    @DecimalMax(value = "70.0", message = "Fat percentage must be realistic")
    private Double fatPercentage;

    @NotNull(message = "Gender is required")
    private Gender gender;


    private ActivityLevel activityLevel;

    @Size(max = 500, message = "Medical notes must not exceed 500 characters")
    private String medicalNotes;
    
    private DietType dietType = DietType.BALANCED;

    @NotNull(message = "Diet goal is required")
    private DietGoal dietGoal;

    @DecimalMin(value = "20.0", message = "Target weight must be at least 20 kg")
    @DecimalMax(value = "500.0", message = "Target weight must be realistic")
    private Double targetWeightKg;

    @Min(value = 1000, message = "Daily calorie target must be at least 1000 kcal")
    @Max(value = 10000, message = "Daily calorie target must be realistic")
    private Integer dailyCalorieTarget;
    
    // private List<AllergenType> allergenTypes;
}
