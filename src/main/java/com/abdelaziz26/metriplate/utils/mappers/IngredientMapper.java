package com.abdelaziz26.metriplate.utils.mappers;

import com.abdelaziz26.metriplate.dtos.ingredients.CreateIngredientDto;
import com.abdelaziz26.metriplate.dtos.ingredients.IngredientDto;
import com.abdelaziz26.metriplate.dtos.ingredients.ReadIngredientDto;
import com.abdelaziz26.metriplate.dtos.ingredients.UpdateIngredientDto;
import com.abdelaziz26.metriplate.dtos.nutrient.NutrientDto;
import com.abdelaziz26.metriplate.dtos.tag.DietaryTagDto;
import com.abdelaziz26.metriplate.entities.DietaryTag;
import com.abdelaziz26.metriplate.entities.Ingredient;
import com.abdelaziz26.metriplate.entities.Nutrient;
import com.abdelaziz26.metriplate.enums.IngredientCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class IngredientMapper {

    private final NutrientMapper nutrientMapper;
    private final DietTagMapper dietTagMapper;

    public Ingredient toEntity(CreateIngredientDto dto, List<Nutrient> nutrients, List<DietaryTag> dietaryTags) {
              return Ingredient.builder()
                      .name(dto.getName())
                      .fat(dto.getFat())
                      .fiber(dto.getFiber())
                      .sugar(dto.getSugar())
                      .carbohydrates(dto.getCarbohydrates())
                      .protein(dto.getProtein())
                      .calories(dto.getCalories())
                      .category(IngredientCategory.valueOf(dto.getCategory()))
                      .description(dto.getDescription())
                      .dietaryTags(dietaryTags)
                      .nutrients(nutrients)
                      .build();
    }

    public Ingredient toEntity(UpdateIngredientDto dto, Ingredient entity) {

        if(dto.getName() != null)
          entity.setName(dto.getName());

        if (dto.getFat() != null)
          entity.setFat(dto.getFat());

        if (dto.getFiber() != null)
          entity.setFiber(dto.getFiber());

        if (dto.getSugar() != null)
          entity.setSugar(dto.getSugar());

        if (dto.getCarbohydrates() != null)
          entity.setCarbohydrates(dto.getCarbohydrates());

        if (dto.getProtein() != null)
            entity.setProtein(dto.getProtein());

        if (dto.getCalories() != null)
          entity.setCalories(dto.getCalories());

        if (dto.getDescription() != null)
          entity.setDescription(dto.getDescription());

        if (dto.getCategory() != null)
          entity.setCategory(IngredientCategory.valueOf(dto.getCategory()));

        return entity;
    }

    public ReadIngredientDto toDto(Ingredient entity) {
        return ReadIngredientDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .fat(entity.getFat())
                .fiber(entity.getFiber())
                .sugar(entity.getSugar())
                .category(entity.getCategory().name())
                .carbohydrates(entity.getCarbohydrates())
                .protein(entity.getProtein())
                .calories(entity.getCalories())
                .description(entity.getDescription())

                .nutrients(entity.getNutrients().stream()
                        .map(nutrientMapper::toSummary).collect(Collectors.toList())
                )
                .dietaryTags(entity.getDietaryTags().stream()
                        .map(dietTagMapper::toSummary).collect(Collectors.toList())
                )
                .build();
    }

    public IngredientDto toSummary(Ingredient entity) {
                return IngredientDto.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .build();
    }
}
