package com.abdelaziz26.metriplate.controllers;

import com.abdelaziz26.metriplate.dtos.ingredients.CreateIngredientDto;
import com.abdelaziz26.metriplate.dtos.ingredients.IngredientDto;
import com.abdelaziz26.metriplate.dtos.ingredients.ReadIngredientDto;
import com.abdelaziz26.metriplate.dtos.ingredients.UpdateIngredientDto;
import com.abdelaziz26.metriplate.enums.DietaryTagType;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.services.ingredient.IngredientService;
import com.abdelaziz26.metriplate.utils._Abdel3zizController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController extends _Abdel3zizController {
    private final IngredientService ingredientService;

    @GetMapping("/{id}")
    public ResponseEntity<@NotNull Result<ReadIngredientDto, Error>> getIngredientById(@PathVariable long id) {
        Result<ReadIngredientDto, Error> result = ingredientService.getById(id);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<@NotNull Result<ReadIngredientDto, Error>> getIngredientByName(@PathVariable String name) {
        Result<ReadIngredientDto, Error> result = ingredientService.getByName(name);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @GetMapping
    public ResponseEntity<@NotNull Result<List<IngredientDto>, Error>> getAllIngredients(
            @RequestParam(defaultValue = "1") int pageIdx,
            @RequestParam(required = false) List<DietaryTagType> dietaryTagTypes) {
        Result<List<IngredientDto>, Error> result;
        if (dietaryTagTypes != null && !dietaryTagTypes.isEmpty()) {
            result = ingredientService.getAll(pageIdx, dietaryTagTypes);
        } else {
            result = ingredientService.getAll(pageIdx);
        }
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @PostMapping
    public ResponseEntity<@NotNull Result<ReadIngredientDto, Error>> addIngredient(@Valid @RequestBody CreateIngredientDto createDto) {
        Result<ReadIngredientDto, Error> result = ingredientService.addIngredient(createDto);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<@NotNull Result<ReadIngredientDto, Error>> updateIngredient(@PathVariable Long id, @Valid @RequestBody UpdateIngredientDto updateDto) {
        Result<ReadIngredientDto, Error> result = ingredientService.updateIngredient(id, updateDto);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<@NotNull Result<String, Error>> deleteIngredient(@PathVariable Long id) {
        Result<String, Error> result = ingredientService.deleteIngredient(id);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @PostMapping("/{ingredientId}/tags")
    public ResponseEntity<@NotNull Result<String, Error>> addIngredientTags(@PathVariable Long ingredientId, @RequestBody List<Long> tagIds) {
        Result<String, Error> result = ingredientService.addIngredientTag(ingredientId, tagIds);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @DeleteMapping("/{ingredientId}/tags")
    public ResponseEntity<@NotNull Result<String, Error>> removeIngredientTags(@PathVariable Long ingredientId, @RequestBody List<Long> tagIds) {
        Result<String, Error> result = ingredientService.removeIngredientTag(ingredientId, tagIds);
        return new ResponseEntity<>(result, resolveStatus(result));
    }
}