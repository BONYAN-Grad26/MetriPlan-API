package com.abdelaziz26.metriplate.dtos.nutrient;

import com.abdelaziz26.metriplate.enums.NutrientType;
import com.abdelaziz26.metriplate.utils.annotations.ValidateEnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UpdateNutrientDTO {

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @ValidateEnumValue(enumClass = NutrientType.class)
    @NotBlank
    private String nutrientType;

    @Size(max = 20, message = "Unit must be at most 20 characters")
    private String unit;

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    private Double rdaValue;

    @Size(max = 20, message = "RDA unit must be at most 20 characters")
    private String rdaUnit;
}
