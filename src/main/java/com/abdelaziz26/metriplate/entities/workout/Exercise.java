package com.abdelaziz26.metriplate.entities.workout;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "exercises")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Exercise {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private Integer sets;
    private String reps;
    private Integer restSeconds;

    @Column(length = 1000)
    private String notes;

    @ManyToOne
    @JoinColumn(name = "day_id")
    private WorkoutDay day;
}
