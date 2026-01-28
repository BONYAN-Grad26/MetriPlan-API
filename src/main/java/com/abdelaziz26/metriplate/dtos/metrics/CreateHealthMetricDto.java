package com.abdelaziz26.metriplate.dtos.metrics;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor @AllArgsConstructor
@Getter
@Setter
public class CreateHealthMetricDto {

    private LocalDate recordedAt;

    private Double bodyFatPercentage;

    private Double muscleMass;

    private Double bmi;

    private Double waistCircumference;

    private Double hipCircumference;

    private String notes;
}
