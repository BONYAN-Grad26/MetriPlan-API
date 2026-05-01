package com.abdelaziz26.metriplate.dtos.ingredients;

import com.abdelaziz26.metriplate.dtos.allergy.ReadAllergyDto;
import com.abdelaziz26.metriplate.dtos.tag.DietaryTagDto;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ReadIngredientDto {
    private Long id;

    private String name;

    private String imageUrl;

    private String category;

    private Double calories;
    private Double proteinG;
    private Double carbsG;
    private Double fatG;
    private Double fiberG;
    private Double sugarG;

    private List<DietaryTagDto> dietaryTags;
    private List<ReadAllergyDto> allergens;

    // E-commerce fields
    private BigDecimal price;
    private String unit;
    private Integer stockQuantity;
    private Boolean availableForSale;
    private LocalDateTime createdAt;
}
