package com.abdelaziz26.metriplate.dtos.workout;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseDto {
    private String name;
    private int sets;
    private String reps;
    private int rest_seconds;
    private String notes;
}
