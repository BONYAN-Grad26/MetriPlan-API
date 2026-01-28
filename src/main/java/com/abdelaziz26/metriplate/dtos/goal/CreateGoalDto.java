package com.abdelaziz26.metriplate.dtos.goal;

import com.abdelaziz26.metriplate.enums.GoalType;
import com.abdelaziz26.metriplate.utils.annotations.ValidateEnumValue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor @AllArgsConstructor
@Getter
@Setter
public class CreateGoalDto {

    @ValidateEnumValue(enumClass = GoalType.class)
    @NotBlank
    private String goalType;

    @NotBlank
    private String description;

    // Target metrics
    @NotNull(message = "Target weight is required")
    @Positive(message = "Target weight must be positive")
    private Double targetWeight;

    @NotNull(message = "Target body fat is required")
    @Positive(message = "Target body fat must be positive")
    private Double targetBodyFat;

    @Future
    @NotNull(message = "Target date is required")
    private LocalDate startDate;

    @Future
    @NotNull(message = "Target date is required")
    private LocalDate targetDate;
}
