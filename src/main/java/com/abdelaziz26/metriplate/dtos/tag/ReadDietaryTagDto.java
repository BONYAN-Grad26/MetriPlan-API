package com.abdelaziz26.metriplate.dtos.tag;

import com.abdelaziz26.metriplate.dtos.ingredients.IngredientDto;
import com.abdelaziz26.metriplate.entities.Ingredient;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadDietaryTagDto extends DietaryTagDto {

    private Long id;

    private String name;

    private String type;

    private String description;

    private List<IngredientDto> ingredients;
}
