package com.abdelaziz26.metriplate.entities;

import com.abdelaziz26.metriplate.enums.AllergenType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity @Table(name = "allergies")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Allergy {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AllergenType type;

    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nutrient_id", nullable = false)
    private Nutrient nutrient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
