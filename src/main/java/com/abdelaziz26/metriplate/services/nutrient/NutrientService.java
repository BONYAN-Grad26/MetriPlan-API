package com.abdelaziz26.metriplate.services.nutrient;

import com.abdelaziz26.metriplate.dtos.nutrient.CreateNutrientDto;
import com.abdelaziz26.metriplate.dtos.nutrient.NutrientDto;
import com.abdelaziz26.metriplate.dtos.nutrient.ReadNutrientDto;
import com.abdelaziz26.metriplate.dtos.nutrient.UpdateNutrientDTO;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;

import java.util.List;

public interface NutrientService {
    Result<List<NutrientDto>, Error> getAllNutrients();
    Result<ReadNutrientDto, Error> getById(Long id);
    Result<ReadNutrientDto, Error> getByType(String type);

    Result<ReadNutrientDto, Error> addNutrient(CreateNutrientDto createNutrientDto);
    Result<ReadNutrientDto, Error> updateNutrient(Long id, UpdateNutrientDTO updateNutrientDTO);

    Result<String, Error> deleteNutrient(Long id);
}
