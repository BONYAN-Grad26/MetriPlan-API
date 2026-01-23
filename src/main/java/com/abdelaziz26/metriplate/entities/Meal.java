package com.abdelaziz26.metriplate.entities;

import com.abdelaziz26.metriplate.enums.MealType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity @Table(name = "meals")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    private String description;
    private String preparationInstructions;
    private Integer preparationTime; // in minutes

    @OneToMany(mappedBy = "meal", cascade =  CascadeType.ALL, orphanRemoval = true)
    private List<MealIngredient> ingredients;
}
