package com.abdelaziz26.metriplate.dtos.plan;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor
public class WeekDTO {
    // Optional id for mapping to WeeklyPlan entity
    private Long id;

    private Integer weekNumber;
    private String startDate;
    private String endDate;
    private Double weeklyCalorieTarget;
    private Double weeklyProteinTarget;
    private Double weeklyCarbTarget;
    private Double weeklyFatTarget;
    private String weeklyStrategy;
    private String aiPreparationTips;

    private List<DayDTO> days;
}
