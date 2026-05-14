package com.abdelaziz26.metriplate.dtos.workout;

import lombok.*;

import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class WorkoutPlanResponseDto {
    private String plan_name;
    private String split_type;
    private String split_reasoning;
    private Map<String, WorkoutDayDto> weekly_schedule;
}
