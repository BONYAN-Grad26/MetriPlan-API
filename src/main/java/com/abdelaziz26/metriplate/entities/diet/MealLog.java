package com.abdelaziz26.metriplate.entities.diet;

import com.abdelaziz26.metriplate.enums.diet.MealLogStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "meal_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_progress_log_id", nullable = false)
    private DailyProgressLog dailyProgressLog;

    // The planned meal this log entry refers to (nullable: user may log unplanned food)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_plan_meal_id")
    private DailyPlanMeal dailyPlanMeal;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MealLogStatus status = MealLogStatus.EATEN_AS_PLANNED;

    // Only populated for EATEN_MODIFIED or SUBSTITUTED; otherwise inherited from the plan
    private Double actualCalories;

    private Double actualProtein;

    private Double actualCarbs;

    private Double actualFat;

    // e.g. 0.5 = half portion, 1.5 = extra serving (applied on top of plan values)
    @Builder.Default
    private Double portionMultiplier = 1.0;

    @Column(length = 500)
    private String substituteFood;

    private LocalTime eatenAt;

    @Column(length = 500)
    private String userNotes;
}
