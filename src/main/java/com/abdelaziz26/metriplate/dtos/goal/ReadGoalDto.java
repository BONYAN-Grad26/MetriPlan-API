package com.abdelaziz26.metriplate.dtos.goal;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter
@Setter
@Builder
public class ReadGoalDto {

    private Long id;

    private String goalType;

    private String description;

    private Long userId;

    // Target metrics
    private Double targetWeight;

    private Double targetBodyFat;

    private Double targetCalories;

    private LocalDate startDate;
    private LocalDate targetDate;

    private String status;

    private Long totalDietPlans;

    private Long completedDietPlans;

    private List<Long> dietPlanIds;

}
