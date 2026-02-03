package com.abdelaziz26.metriplate.dtos.plan;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor
public class DayDTO {

    private String date;
    private Integer dayOfWeek;
    private Double targetCalories;
    private Double targetProtein;
    private Double targetCarbs;
    private Double targetFat;
    private Double targetFiber;
    private Double targetSugar;
    private Double waterGoal;
    private String aiDailyTips;

    private List<MealDTO> meals;
}
