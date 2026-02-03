package com.abdelaziz26.metriplate.dtos.goal;

import com.abdelaziz26.metriplate.enums.GoalType;
import com.abdelaziz26.metriplate.utils.annotations.ValidateEnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateGoalDto {

    @ValidateEnumValue(enumClass = GoalType.class)
    @NotBlank
    private String goalType;

    @NotBlank
    private String description;

    @NotNull
    private Double targetWeight;

    @NotNull
    private Double targetBodyFat;


    private LocalDate startDate;

    private LocalDate targetDate;
}
