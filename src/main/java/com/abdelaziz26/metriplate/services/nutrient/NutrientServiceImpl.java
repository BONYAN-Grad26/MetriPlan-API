package com.abdelaziz26.metriplate.services.nutrient;

import com.abdelaziz26.metriplate.dtos.nutrient.CreateNutrientDto;
import com.abdelaziz26.metriplate.dtos.nutrient.NutrientDto;
import com.abdelaziz26.metriplate.dtos.nutrient.ReadNutrientDto;
import com.abdelaziz26.metriplate.dtos.nutrient.UpdateNutrientDTO;
import com.abdelaziz26.metriplate.entities.Nutrient;
import com.abdelaziz26.metriplate.enums.NutrientType;
import com.abdelaziz26.metriplate.repositories.NutrientRepository;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Errors;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.utils.mappers.NutrientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("NutrientService")
@RequiredArgsConstructor
public class NutrientServiceImpl implements NutrientService {

    private final NutrientRepository nutrientRepository;
    private final NutrientMapper nutrientMapper;

    @Override
    public Result<List<NutrientDto>, Error> getAllNutrients() {
        return Result.CreateSuccessResult(nutrientRepository.findAll().stream().map(nutrientMapper::toSummary).toList());
    }

    @Override
    public Result<ReadNutrientDto, Error> getById(Long id) {
        return nutrientRepository.findById(id).map(n ->
                Result.CreateSuccessResult(nutrientMapper.toDto(n)) )
                .orElse( Result.CreateErrorResult( Errors.NotFoundErr("Nutrient with id " + id + " not found.")
                        )
                );
    }

    @Override
    public Result<ReadNutrientDto, Error> getByType(String type) {
        return nutrientRepository.findByNutrientType(NutrientType.valueOf(type.toUpperCase()))
                .map(n -> Result.CreateSuccessResult(nutrientMapper.toDto(n)))
                .orElse( Result.CreateErrorResult( Errors.NotFoundErr("Nutrient with type " + type + " not found.")
                        )
                );
    }

    @Override
    public Result<ReadNutrientDto, Error> addNutrient(CreateNutrientDto createNutrientDto) {


        boolean exists = nutrientRepository.existsByNutrientTypeOrName(
                NutrientType.valueOf(createNutrientDto.getNutrientType().toUpperCase()),
                createNutrientDto.getName()
        );

        if (exists) {
            return Result.CreateErrorResult( Errors.BadRequestErr("Nutrient with type " + createNutrientDto.getNutrientType() + " and name " + createNutrientDto.getName() + " already exists.")
                    );
        }

        Nutrient nutrient = nutrientMapper.toEntity(createNutrientDto);
        nutrientRepository.save(nutrient);
        return Result.CreateSuccessResult(nutrientMapper.toDto(nutrient));
    }

    @Override
    public Result<ReadNutrientDto, Error> updateNutrient(Long id, UpdateNutrientDTO updateNutrientDTO) {
        Nutrient nutrient = nutrientRepository.findById(id).orElse(null);

        if (nutrient == null) {
            return Result.CreateErrorResult( Errors.NotFoundErr("Nutrient with id " + id + " not found.")
                    );
        }

        nutrient = nutrientMapper.toEntity(updateNutrientDTO, nutrient);
        nutrientRepository.save(nutrient);

        return Result.CreateSuccessResult(nutrientMapper.toDto(nutrient));
    }

    @Override
    public Result<String, Error> deleteNutrient(Long id) {
        if (!nutrientRepository.existsById(id)) {
            return Result.CreateErrorResult( Errors.NotFoundErr("Nutrient with id " + id + " not found.")
                    );
        }

        nutrientRepository.deleteById(id);
        return Result.CreateSuccessResult("Nutrient with id " + id + " deleted successfully.");
    }
}
