package com.abdelaziz26.metriplate.dtos.workout;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutDayDto {
    private String session;
    private String focus;
    private List<ExerciseDto> exercises;
}
