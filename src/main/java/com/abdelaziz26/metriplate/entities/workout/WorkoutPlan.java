package com.abdelaziz26.metriplate.entities.workout;

import com.abdelaziz26.metriplate.entities.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "workout_week_plans")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String planName;
    private String splitType;
    private String splitReasoning;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<WorkoutDay> days;
}
