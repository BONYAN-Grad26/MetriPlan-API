package com.abdelaziz26.metriplate.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "weekly_plan")
@NoArgsConstructor @AllArgsConstructor
@Getter
@Setter
public class WeeklyPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer weekNumber;

    private LocalDate startDate;
    private LocalDate endDate;

    // Weekly nutritional targets (AI-calculated)
    private Double weeklyCalorieTarget;

    private Double weeklyProteinTarget;

    private Double weeklyCarbTarget;

    private Double weeklyFatTarget;


    @Column(length = 2000)
    private String weeklyStrategy; // AI-generated strategy description

    // Daily plans
    @OneToMany(mappedBy = "weeklyPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyPlan> dailyPlans = new ArrayList<>();

    @ManyToOne
    @JoinColumn(nullable = false, name = "diet_plan_id")
    private DietPlan dietPlan;

    @Column(length = 1000)
    private String aiPreparationTips;

    private Double actualCaloriesConsumed;

    private Double actualProteinConsumed;

    // Weekly metrics (calculated)
    private Double averageDailyCalories;

    private Double adherenceRate; // Percentage of meals followed

    // User feedback
    private Integer userRating;

    @Column(length = 2000)
    private String userNotes;

}
