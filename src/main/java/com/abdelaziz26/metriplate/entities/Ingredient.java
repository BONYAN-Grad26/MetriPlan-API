package com.abdelaziz26.metriplate.entities;

import com.abdelaziz26.metriplate.enums.IngredientCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity @Table(name = "ingredients", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class Ingredient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;                   // "Oats", "Chicken Breast", "Almond Milk"

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IngredientCategory category;   // Dairy, Grains, Protein, Vegetables …

    // ── Nutritional Info (per 100 g / ml) ─────
    private Double calories;
    private Double proteinG;               // renamed from proteinG to proteinG for consistency
    private Double carbsG;                 // renamed from carbsG to carbsG for consistency
    private Double fatG;                   // renamed from fatG to fatG for consistency
    private Double fiberG;                 // renamed from fiberG to fiberG for consistency
    private Double sugarG;                 // added sugar field


    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Allergy> allergens = new ArrayList<>() {
    };

    // ── E-commerce fields ─────────────────────
    @Column(nullable = false)
    private BigDecimal price;

    private String unit;                   // "kg", "g", "piece", "liter"
    private Integer stockQuantity;

    private boolean availableForSale;      // admin toggle

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "ingredient", cascade =  CascadeType.ALL, orphanRemoval = true)
    private List<MealIngredient> ingredients;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "ingredient_dietary_tags",
            joinColumns = @JoinColumn(name = "ingredient_id"),
            inverseJoinColumns = @JoinColumn(name = "dietary_tag_id")
    )
    private List<DietaryTag> dietaryTags = new ArrayList<>();
}
