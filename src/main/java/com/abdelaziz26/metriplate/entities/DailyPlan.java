package com.abdelaziz26.metriplate.entities;

import com.abdelaziz26.metriplate.enums.DayStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "daily_plan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weekly_plan_id", nullable = false)
    private WeeklyPlan weeklyPlan;

    private LocalDate date;
    private Integer dayOfWeek; // 1-7 (Monday = 1)

    // Target nutritional goals (AI-generated)
    private Double targetCalories;

    private Double targetProtein;

    private Double targetCarbs;

    private Double targetFat;

    private Double targetFiber;

    private Double targetSugar;

    private Double waterGoal; // in ml

    private Double waterConsumed;

    // Meals for this day (AI-suggested)
    @OneToMany(mappedBy = "dailyPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyPlanMeal> meals = new ArrayList<>();

    // Actual consumption (user-tracked)
    private Double actualCaloriesConsumed;

    private Double actualProteinConsumed;

    private Double actualCarbsConsumed;

    private Double actualFatConsumed;

    // AI-specific fields
    private String aiDailyTheme; // e.g., "High Protein Day", "Carb Cycling Day"

    @Column(length = 1000)
    private String aiDailyTips; // AI-generated tips for the day

    @Column(length = 500)
    private String aiShoppingNotes; // Special items needed for this day

    // User feedback
    private Integer userRating; // 1-5 stars
    private Boolean userFollowedPlan;

    @Column(length = 1000)
    private String userNotes;

    // Status tracking
    @Enumerated(EnumType.STRING)
    private DayStatus status = DayStatus.PLANNED;

}
