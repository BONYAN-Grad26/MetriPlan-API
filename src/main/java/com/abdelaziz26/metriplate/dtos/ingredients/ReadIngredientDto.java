package com.abdelaziz26.metriplate.dtos.ingredients;

import com.abdelaziz26.metriplate.dtos.nutrient.NutrientDto;
import com.abdelaziz26.metriplate.dtos.tag.DietaryTagDto;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ReadIngredientDto {
    private Long id;

    private String name;

    private String description;

    private String category;

    private Double calories;
    private Double protein;
    private Double carbohydrates;
    private Double fat;
    private Double fiber;
    private Double sugar;

    private List<NutrientDto> nutrients;
    private List<DietaryTagDto> dietaryTags;
}
