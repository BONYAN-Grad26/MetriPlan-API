package com.abdelaziz26.metriplate.entities;

import com.abdelaziz26.metriplate.enums.DietaryTagType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "dietary_tag")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DietaryTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DietaryTagType type;

    private String name;
    private String description;

    @Builder.Default
    @ManyToMany(mappedBy = "dietaryTags")
    private List<Ingredient> ingredients = new ArrayList<>();
}
