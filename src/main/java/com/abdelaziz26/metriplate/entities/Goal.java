package com.abdelaziz26.metriplate.entities;

import com.abdelaziz26.metriplate.enums.GoalType;
import com.abdelaziz26.metriplate.enums.ProgressStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "goals")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Goal {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private GoalType goalType;

    private String description;

    // Target metrics
    private Double targetWeight;
    private Double targetBodyFat;
    private Double targetCalories;

    private LocalDate startDate;
    private LocalDate targetDate;

    @Enumerated(EnumType.STRING)
    private ProgressStatus status;

    @OneToMany(mappedBy = "goal")
    private List<DietPlan> dietPlans;
}
