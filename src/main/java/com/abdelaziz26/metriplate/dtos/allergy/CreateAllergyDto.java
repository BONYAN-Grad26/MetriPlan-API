package com.abdelaziz26.metriplate.dtos.allergy;

import com.abdelaziz26.metriplate.enums.AllergenType;
import com.abdelaziz26.metriplate.utils.annotations.ValidateEnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAllergyDto {

    @NotBlank
    private String type;

    @NotNull
    private Long nutrientId;

    @ValidateEnumValue(enumClass = AllergenType.class)
    private String name;

    private String description;
}
