package com.abdelaziz26.metriplate.entities.workout;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "workout_day_plans")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutDay {
    @Id
    @GeneratedValue
    private Long id;

    private String dayName;
    private String session;
    private String focus;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private WorkoutPlan plan;

    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL)
    private List<Exercise> exercises;

}
