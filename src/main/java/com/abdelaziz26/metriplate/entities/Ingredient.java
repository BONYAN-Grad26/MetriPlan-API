package com.abdelaziz26.metriplate.entities;

import com.abdelaziz26.metriplate.enums.IngredientCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "ingredients", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
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
    private Double calories;

    private Double protein; // in grams

    private Double carbohydrates;

    private Double fat;

    private Double fiber;

    private Double sugar;

    @OneToMany(mappedBy = "ingredient", cascade =  CascadeType.ALL, orphanRemoval = true)
    private List<MealIngredient> ingredients;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "ingredient_nutrients",
            joinColumns = @JoinColumn(name = "ingredient_id"),
            inverseJoinColumns = @JoinColumn(name = "nutrient_id")
    )
    private List<Nutrient> nutrients = new ArrayList<>();;

    @Builder.Default    //   --------->   مهمه جدا
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "ingredient_dietary_tags",
            joinColumns = @JoinColumn(name = "ingredient_id"),
            inverseJoinColumns = @JoinColumn(name = "dietary_tag_id")
    )
    private List<DietaryTag> dietaryTags = new ArrayList<>();
}
