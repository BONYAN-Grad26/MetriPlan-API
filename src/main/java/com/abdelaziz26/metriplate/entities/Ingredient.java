package com.abdelaziz26.metriplate.entities;

import com.abdelaziz26.metriplate.enums.IngredientCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity @Table(name = "ingredients")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private IngredientCategory category;

    // Per 100g measurements
    @Column(precision = 10, scale = 2)
    private Double calories;

    @Column(precision = 10, scale = 2)
    private Double protein; // in grams

    @Column(precision = 10, scale = 2)
    private Double carbohydrates;

    @Column(precision = 10, scale = 2)
    private Double fat;

    @Column(precision = 10, scale = 2)
    private Double fiber;

    @Column(precision = 10, scale = 2)
    private Double sugar;

    @OneToMany(mappedBy = "ingredient", cascade =  CascadeType.ALL, orphanRemoval = true)
    private List<MealIngredient> ingredients;

    @ManyToMany
    @JoinTable(
            name = "ingredient_nutrients",
            joinColumns = @JoinColumn(name = "ingredient_id"),
            inverseJoinColumns = @JoinColumn(name = "nutrient_id")
    )
    private List<Nutrient> nutrients;

    @ManyToMany
    @JoinTable(
            name = "ingredient_dietary_tags",
            joinColumns = @JoinColumn(name = "ingredient_id"),
            inverseJoinColumns = @JoinColumn(name = "dietary_tag_id")
    )
    private List<DietaryTag> dietaryTags;
}
