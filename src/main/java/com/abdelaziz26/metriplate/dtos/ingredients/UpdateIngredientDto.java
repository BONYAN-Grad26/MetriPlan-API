package com.abdelaziz26.metriplate.dtos.ingredients;

import com.abdelaziz26.metriplate.enums.IngredientCategory;
import com.abdelaziz26.metriplate.utils.annotations.ValidateEnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class UpdateIngredientDto {

    private String name;

    private String description;

    @ValidateEnumValue(enumClass = IngredientCategory.class)
    private String category;

    private Double calories;
    private Double protein;
    private Double carbohydrates;
    private Double fat;
    private Double fiber;
    private Double sugar;

    private List<Long> nutrientIds;
    private List<Long> dietaryTagIds;
}
