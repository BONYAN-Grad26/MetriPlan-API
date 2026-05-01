package com.abdelaziz26.metriplate.services.allergy;

import com.abdelaziz26.metriplate.dtos.allergy.CreateAllergyDto;
import com.abdelaziz26.metriplate.dtos.allergy.ReadAllergyDto;
import com.abdelaziz26.metriplate.dtos.allergy.UpdateAllergyDto;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;

import java.util.List;

public interface AllergyService {
    Result<ReadAllergyDto, Error> getById(Long id);
    Result<List<ReadAllergyDto>, Error> getByUserId(Long userId);

    Result<ReadAllergyDto, Error> addAllergy(CreateAllergyDto dto);
    Result<ReadAllergyDto, Error> updateAllergy(Long id, UpdateAllergyDto dto);

    Result<String, Error> deleteAllergy(Long id);


}
