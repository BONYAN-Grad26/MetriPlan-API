package com.abdelaziz26.metriplate.entities;

import com.abdelaziz26.metriplate.enums.DietaryTagType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity @Table(name = "dietary_tag")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class DietaryTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DietaryTagType type;

    private String name;
    private String description;

    @ManyToMany(mappedBy = "dietaryTags")
    private List<Ingredient> ingredients;
}
