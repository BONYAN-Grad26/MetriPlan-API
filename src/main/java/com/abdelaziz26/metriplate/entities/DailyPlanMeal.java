package com.abdelaziz26.metriplate.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "daily_plan_meals")
@NoArgsConstructor @AllArgsConstructor
@Getter
@Setter
public class DailyPlanMeal {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer mealOrder;

    private Boolean isCustomized;

    private String customNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_plan_id")
    private DailyPlan dailyPlan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "meal_id")
    private Meal meal;
}
