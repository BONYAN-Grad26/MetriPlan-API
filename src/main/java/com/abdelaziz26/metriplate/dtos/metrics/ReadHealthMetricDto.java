package com.abdelaziz26.metriplate.dtos.metrics;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ReadHealthMetricDto {

    private Long id;

    private LocalDate recordedAt;

    private Double bodyFatPercentage;

    private Double muscleMass;

    private Double bmi;

    private Double waistCircumference;

    private Double hipCircumference;

    private String notes;

    private String bmiCategory;
}
