package com.abdelaziz26.metriplate.dtos.tag;

import com.abdelaziz26.metriplate.enums.DietaryTagType;
import com.abdelaziz26.metriplate.utils.annotations.ValidateEnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor  @AllArgsConstructor
public class UpdateDietaryTagDto {

    @ValidateEnumValue(enumClass = DietaryTagType.class)
    @NotBlank
    private String type;

    @NotBlank
    private String name;

    @Size(max = 300)
    private String description;
}
