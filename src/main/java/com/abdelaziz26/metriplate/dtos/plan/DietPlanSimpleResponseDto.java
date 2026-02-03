package com.abdelaziz26.metriplate.dtos.plan;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class DietPlanSimpleResponseDto {
    private Long id;
    private String planName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer durationInWeeks;
    private String dietType;
    private Integer mealsPerDay;
    private Boolean includeSnacks;
    private Double dailyCalorieTarget;
    private Double proteinPercentage;
    private Double carbPercentage;
    private Double fatPercentage;
    private String aiSuccessTips;
    private String status;
}
