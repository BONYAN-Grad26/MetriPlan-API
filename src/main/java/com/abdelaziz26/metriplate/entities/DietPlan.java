package com.abdelaziz26.metriplate.entities;

import com.abdelaziz26.metriplate.enums.DietType;
import com.abdelaziz26.metriplate.enums.PlanStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "diet_plans")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DietPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;

    @Column(nullable = false)
    private String planName;

    @Column(length = 2000)
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;
    private Integer durationInWeeks;

    // Plan configuration (AI will use these)
    @Enumerated(EnumType.STRING)
    private DietType dietType;

    private Integer mealsPerDay = 3;
    private Boolean includeSnacks = true;

    // Nutritional configuration
    private Double dailyCalorieTarget;

    private Double proteinPercentage;

    private Double carbPercentage;

    private Double fatPercentage;

    @Column(length = 2000)
    private String aiSuccessTips;

    @OneToMany(mappedBy = "dietPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("weekNumber ASC")
    private List<WeeklyPlan> weeklyPlans = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private PlanStatus status = PlanStatus.ACTIVE;

}
