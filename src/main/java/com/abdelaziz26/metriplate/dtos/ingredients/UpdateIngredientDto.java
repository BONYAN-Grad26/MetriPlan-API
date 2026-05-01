package com.abdelaziz26.metriplate.dtos.ingredients;

import com.abdelaziz26.metriplate.enums.IngredientCategory;
import com.abdelaziz26.metriplate.utils.annotations.ValidateEnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class UpdateIngredientDto {

    private String name;

    private String imageUrl;

    @ValidateEnumValue(enumClass = IngredientCategory.class)
    private String category;

    private Double calories;
    private Double proteinG;
    private Double carbsG;
    private Double fatG;
    private Double fiberG;
    private Double sugarG;

    private List<Long> dietaryTagIds;
    private List<Long> allergenIds;

    // E-commerce fields
    private BigDecimal price;
    private String unit;
    private Integer stockQuantity;
    private Boolean availableForSale;
}
