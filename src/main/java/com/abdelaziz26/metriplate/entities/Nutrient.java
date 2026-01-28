package com.abdelaziz26.metriplate.entities;

import com.abdelaziz26.metriplate.enums.NutrientType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.List;

@Entity @Table(name = "nutrients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Nutrient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private NutrientType nutrientType;

    private String unit;
    private String description;

    // Recommended Daily Allowance for average adult
     private Double rdaValue;
     private String rdaUnit;

    @ManyToMany(mappedBy = "nutrients")
    private List<Ingredient> ingredients;

    @OneToMany(mappedBy = "nutrient", cascade =  CascadeType.ALL, orphanRemoval = true)
    private List<Allergy>  allergies;
}
