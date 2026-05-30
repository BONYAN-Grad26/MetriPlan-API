package com.abdelaziz26.metriplate.entities.diet;

import com.abdelaziz26.metriplate.enums.user.DailyComplianceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "daily_progress_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyProgressLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_plan_id", nullable = false, unique = true)
    private DailyPlan dailyPlan;

    private LocalDate date;

    // Actual macros consumed (aggregated from MealLogs)
    private Double caloriesConsumed;

    private Double proteinConsumed;

    private Double carbsConsumed;

    private Double fatConsumed;

    private Double waterConsumed; // ml

    // Calculated: 0.0 – 100.0
    // Formula: (mealsEaten / totalMeals * 60) + (1 - |actualCal - targetCal| / targetCal * 40), clamped
    private Double adherenceScore;

    // Comma-separated meal type names e.g. "BREAKFAST,EVENING_SNACK"
    @Column(length = 500)
    private String missedMeals;

    // Why the user deviated: BUSY, SICK, SOCIAL_EVENT, NOT_HUNGRY, OTHER
    @Column(length = 500)
    private String deviationReason;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DailyComplianceStatus status = DailyComplianceStatus.PLANNED;

    // 1–5: how the user felt on this day
    private Integer userMoodRating;

    @Column(length = 1000)
    private String userNotes;

    private LocalDateTime loggedAt;

    @PrePersist
    protected void onLog() {
        if (loggedAt == null) loggedAt = LocalDateTime.now();
    }

    @Builder.Default
    @OneToMany(mappedBy = "dailyProgressLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealLog> mealLogs = new ArrayList<>();
}
