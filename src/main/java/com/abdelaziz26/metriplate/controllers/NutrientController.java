package com.abdelaziz26.metriplate.controllers;

import com.abdelaziz26.metriplate.dtos.nutrient.CreateNutrientDto;
import com.abdelaziz26.metriplate.dtos.nutrient.NutrientDto;
import com.abdelaziz26.metriplate.dtos.nutrient.ReadNutrientDto;
import com.abdelaziz26.metriplate.dtos.nutrient.UpdateNutrientDTO;
import com.abdelaziz26.metriplate.enums.NutrientType;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.services.nutrient.NutrientService;
import com.abdelaziz26.metriplate.utils._Abdel3zizController;
import com.abdelaziz26.metriplate.utils.annotations.ValidateEnumValue;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nutrient")
@RequiredArgsConstructor
public class NutrientController extends _Abdel3zizController {

    private final NutrientService nutrientService;

    @GetMapping
    public ResponseEntity<@NotNull Result<List<NutrientDto>, Error>> getAllNutrients() {
        Result<List<NutrientDto>, Error> result = nutrientService.getAllNutrients();
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NotNull Result<ReadNutrientDto, Error>> getNutrientById(@PathVariable Long id) {
        Result<ReadNutrientDto, Error> result = nutrientService.getById(id);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<@NotNull Result<ReadNutrientDto, Error>> getNutrientByType(@PathVariable @Valid @ValidateEnumValue(
            enumClass = NutrientType.class
    ) String type) {
        Result<ReadNutrientDto, Error> result = nutrientService.getByType(type);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @PostMapping
    public ResponseEntity<@NotNull Result<ReadNutrientDto, Error>> addNutrient(@Valid @RequestBody CreateNutrientDto createDto) {
        Result<ReadNutrientDto, Error> result = nutrientService.addNutrient(createDto);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<@NotNull Result<ReadNutrientDto, Error>> updateNutrient(@PathVariable Long id, @Valid @RequestBody UpdateNutrientDTO updateDto) {
        Result<ReadNutrientDto, Error> result = nutrientService.updateNutrient(id, updateDto);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<@NotNull Result<String, Error>> deleteNutrient(@PathVariable Long id) {
        Result<String, Error> result = nutrientService.deleteNutrient(id);
        return new ResponseEntity<>(result, resolveStatus(result));
    }
}
