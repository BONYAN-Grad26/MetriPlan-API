package com.abdelaziz26.metriplate.services.ingredient;

import com.abdelaziz26.metriplate.dtos.ingredients.CreateIngredientDto;
import com.abdelaziz26.metriplate.dtos.ingredients.IngredientDto;
import com.abdelaziz26.metriplate.dtos.ingredients.ReadIngredientDto;
import com.abdelaziz26.metriplate.dtos.ingredients.UpdateIngredientDto;
import com.abdelaziz26.metriplate.enums.DietaryTagType;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;

import java.util.List;

public interface IngredientService {
    Result<ReadIngredientDto, Error> getById(long id);
    Result<ReadIngredientDto, Error> getByName(String name);
    Result<List<IngredientDto>, Error> getAll(int pageIdx);
    Result<List<IngredientDto>, Error> getAll(int pageIdx, List<DietaryTagType> dietaryTagTypes);

    Result<ReadIngredientDto, Error> addIngredient(CreateIngredientDto dto);
    Result<ReadIngredientDto, Error> updateIngredient(Long id, UpdateIngredientDto dto);

    Result<String, Error> deleteIngredient(Long id);

    Result<String, Error> addIngredientTag(Long ingredientId, List<Long> tagIds);
    Result<String, Error> removeIngredientTag(Long ingredientId, List<Long> tagIds);
}
