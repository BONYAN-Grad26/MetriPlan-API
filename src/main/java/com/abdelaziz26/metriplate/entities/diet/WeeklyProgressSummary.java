package com.abdelaziz26.metriplate.entities.diet;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "weekly_progress_summary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyProgressSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weekly_plan_id", nullable = false, unique = true)
    private WeeklyPlan weeklyPlan;

    // Aggregated stats (computed from DailyProgressLog records)
    private Double avgDailyCalories;

    private Double avgProteinConsumed;

    private Double avgAdherenceScore; // 0.0 – 100.0

    private Integer daysFullyCompliant; // DailyComplianceStatus == FULLY_COMPLIANT

    private Integer daysPartiallyCompliant;

    private Integer daysMissed;

    // Body metric delta recorded at week end
    private Double weightAtStart; // kg

    private Double weightAtEnd;   // kg

    private Double weightChange;  // weightAtEnd - weightAtStart (negative = loss)

    // Average mood across logged days (1.0 – 5.0)
    private Double avgMoodRating;

    // AI-generated fields (populated by your planning service)
    @Column(length = 3000)
    private String aiInsights; // e.g. "You skipped breakfast 4/7 days..."

    @Column(length = 3000)
    private String aiAdjustmentSuggestions; // e.g. "Increase protein target by 10g next week"

    // Whether the AI suggestions were applied to the next WeeklyPlan
    @Builder.Default
    private Boolean adjustmentsApplied = false;

    private LocalDate generatedAt;

    @PrePersist
    protected void onGenerate() {
        if (generatedAt == null) generatedAt = LocalDate.now();
    }
}
