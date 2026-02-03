package com.abdelaziz26.metriplate.dtos.plan;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor
public class DietPlanDTO {

    private String planName;
    private String description;
    private Double dailyCalorieTarget;
    private Double proteinPercentage;
    private Double carbPercentage;
    private Double fatPercentage;
    private String aiSuccessTips;

    private List<WeekDTO> weeks;
}
