package com.abdelaziz26.metriplate.utils.mappers;

import com.abdelaziz26.metriplate.dtos.ingredients.CreateIngredientDto;
import com.abdelaziz26.metriplate.dtos.ingredients.IngredientDto;
import com.abdelaziz26.metriplate.dtos.ingredients.ReadIngredientDto;
import com.abdelaziz26.metriplate.dtos.ingredients.UpdateIngredientDto;
import com.abdelaziz26.metriplate.entities.Allergy;
import com.abdelaziz26.metriplate.entities.DietaryTag;
import com.abdelaziz26.metriplate.entities.Ingredient;
import com.abdelaziz26.metriplate.enums.IngredientCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class IngredientMapper {

    private final DietTagMapper dietTagMapper;
    private final AllergyMapper allergyMapper;

    public Ingredient toEntity(CreateIngredientDto dto, List<DietaryTag> dietaryTags) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(dto.getName());
        ingredient.setImageUrl(dto.getImageUrl());
        ingredient.setFatG(dto.getFatG());
        ingredient.setFiberG(dto.getFiberG());
        ingredient.setSugarG(dto.getSugarG());
        ingredient.setCarbsG(dto.getCarbsG());
        ingredient.setProteinG(dto.getProteinG());
        ingredient.setCalories(dto.getCalories());
        ingredient.setCategory(IngredientCategory.valueOf(dto.getCategory()));
        ingredient.setPrice(dto.getPrice());
        ingredient.setUnit(dto.getUnit());
        ingredient.setStockQuantity(dto.getStockQuantity());
        ingredient.setAvailableForSale(dto.getAvailableForSale() != null ? dto.getAvailableForSale() : true);
        ingredient.setCreatedAt(LocalDateTime.now());
        ingredient.setDietaryTags(dietaryTags);
        return ingredient;
    }

    public Ingredient toEntity(UpdateIngredientDto dto, Ingredient entity) {

        if(dto.getName() != null)
          entity.setName(dto.getName());

        if(dto.getImageUrl() != null)
          entity.setImageUrl(dto.getImageUrl());

        if (dto.getFatG() != null)
          entity.setFatG(dto.getFatG());

        if (dto.getFiberG() != null)
          entity.setFiberG(dto.getFiberG());

        if (dto.getSugarG() != null)
          entity.setSugarG(dto.getSugarG());

        if (dto.getCarbsG() != null)
          entity.setCarbsG(dto.getCarbsG());

        if (dto.getProteinG() != null)
            entity.setProteinG(dto.getProteinG());

        if (dto.getCalories() != null)
          entity.setCalories(dto.getCalories());

        if (dto.getCategory() != null)
          entity.setCategory(IngredientCategory.valueOf(dto.getCategory()));

        if (dto.getPrice() != null)
          entity.setPrice(dto.getPrice());

        if (dto.getUnit() != null)
          entity.setUnit(dto.getUnit());

        if (dto.getStockQuantity() != null)
          entity.setStockQuantity(dto.getStockQuantity());

        if (dto.getAvailableForSale() != null)
          entity.setAvailableForSale(dto.getAvailableForSale());

        return entity;
    }

    public ReadIngredientDto toDto(Ingredient entity) {
        ReadIngredientDto dto = new ReadIngredientDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setImageUrl(entity.getImageUrl());
        dto.setFatG(entity.getFatG());
        dto.setFiberG(entity.getFiberG());
        dto.setSugarG(entity.getSugarG());
        dto.setCategory(entity.getCategory().name());
        dto.setCarbsG(entity.getCarbsG());
        dto.setProteinG(entity.getProteinG());
        dto.setCalories(entity.getCalories());
        dto.setPrice(entity.getPrice());
        dto.setUnit(entity.getUnit());
        dto.setStockQuantity(entity.getStockQuantity());
        dto.setAvailableForSale(entity.isAvailableForSale());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setDietaryTags(entity.getDietaryTags().stream()
                .map(dietTagMapper::toSummary).collect(Collectors.toList())
        );
        dto.setAllergens(entity.getAllergens().stream()
                .map(allergyMapper::toDto).collect(Collectors.toList())
        );
        return dto;
    }

    public IngredientDto toSummary(Ingredient entity) {
        IngredientDto dto = new IngredientDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}
