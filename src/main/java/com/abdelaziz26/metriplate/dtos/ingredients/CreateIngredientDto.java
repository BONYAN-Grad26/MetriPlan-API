package com.abdelaziz26.metriplate.dtos.ingredients;

import com.abdelaziz26.metriplate.enums.diet.IngredientCategory;
import com.abdelaziz26.metriplate.utils.annotations.ValidateEnumValue;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class CreateIngredientDto {

    @NotBlank
    private String name;

    private String imageUrl = "";

    @ValidateEnumValue(enumClass = IngredientCategory.class)
    @NotBlank
    private String category;

    // Per 100g
    private Double calories;
    private Double proteinG;
    private Double carbsG;
    private Double fatG;
    private Double fiberG;
    private Double sugarG;

    // Relations (IDs only)
    private List<Long> dietaryTagIds;
    private List<Long> allergenIds;

    // E-commerce fields
    private BigDecimal price;
    private String unit;
    private Integer stockQuantity;
    private Boolean availableForSale;

    public static CreateIngredientDto of(CreateIngredientDto dto, String imageUrl) {
        return new CreateIngredientDto(
                dto.getName(),
                imageUrl,
                dto.getCategory(),
                dto.getCalories(),
                dto.getProteinG(),
                dto.getCarbsG(),
                dto.getFatG(),
                dto.getFiberG(),
                dto.getSugarG(),
                dto.getDietaryTagIds(),
                dto.getAllergenIds(),
                dto.getPrice(),
                dto.getUnit(),
                dto.getStockQuantity(),
                dto.getAvailableForSale()
        );
    }

}
