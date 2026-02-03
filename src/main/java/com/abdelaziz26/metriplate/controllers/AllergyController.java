package com.abdelaziz26.metriplate.controllers;

import com.abdelaziz26.metriplate.dtos.allergy.CreateAllergyDto;
import com.abdelaziz26.metriplate.dtos.allergy.ReadAllergyDto;
import com.abdelaziz26.metriplate.dtos.allergy.UpdateAllergyDto;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.services.allergy.AllergyService;
import com.abdelaziz26.metriplate.utils._Abdel3zizController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/allergy")
@RequiredArgsConstructor
public class AllergyController extends _Abdel3zizController {
    private final AllergyService allergyService;

    @GetMapping("/{id}")
    public ResponseEntity<@NotNull Result<ReadAllergyDto, Error>> getAllergyById(@PathVariable Long id) {
        Result<ReadAllergyDto, Error> result = allergyService.getById(id);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<@NotNull Result<List<ReadAllergyDto>, Error>> getAllergiesByUserId(@PathVariable Long userId) {
        Result<List<ReadAllergyDto>, Error> result = allergyService.getByUserId(userId);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @GetMapping("/nutrient/{nutrientId}")
    public ResponseEntity<@NotNull Result<List<ReadAllergyDto>, Error>> getAllergiesByNutrientId(@PathVariable Long nutrientId) {
        Result<List<ReadAllergyDto>, Error> result = allergyService.getByNutrientId(nutrientId);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @PostMapping
    public ResponseEntity<@NotNull Result<ReadAllergyDto, Error>> addAllergy(@Valid @RequestBody CreateAllergyDto createDto) {
        Result<ReadAllergyDto, Error> result = allergyService.addAllergy(createDto);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<@NotNull Result<ReadAllergyDto, Error>> updateAllergy(@PathVariable Long id, @Valid @RequestBody UpdateAllergyDto updateDto) {
        Result<ReadAllergyDto, Error> result = allergyService.updateAllergy(id, updateDto);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<@NotNull Result<String, Error>> deleteAllergy(@PathVariable Long id) {
        Result<String, Error> result = allergyService.deleteAllergy(id);
        return new ResponseEntity<>(result, resolveStatus(result));
    }
}
