package com.abdelaziz26.metriplate.dtos.goal;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor
@Getter
@Setter
@Builder
public class GoalSummaryDto {

    private Long id;

    private String title; // "Lose 5kg in 3 months"

    private Double progressPercentage;

    private String status;

    private Long daysRemaining;

    private String type;
}
