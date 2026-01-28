package com.abdelaziz26.metriplate.utils.mappers;

import com.abdelaziz26.metriplate.dtos.nutrient.CreateNutrientDto;
import com.abdelaziz26.metriplate.dtos.nutrient.NutrientDto;
import com.abdelaziz26.metriplate.dtos.nutrient.ReadNutrientDto;
import com.abdelaziz26.metriplate.dtos.nutrient.UpdateNutrientDTO;
import com.abdelaziz26.metriplate.entities.Nutrient;
import com.abdelaziz26.metriplate.enums.NutrientType;
import org.springframework.stereotype.Service;

@Service
public class NutrientMapper {

    public Nutrient toEntity(CreateNutrientDto nutrientDto) {
        return Nutrient.builder()
                .name(nutrientDto.getName())
                .description(nutrientDto.getDescription())
                .rdaValue(nutrientDto.getRdaValue())
                .rdaUnit(nutrientDto.getRdaUnit())
                .nutrientType(NutrientType.valueOf(nutrientDto.getNutrientType().toUpperCase()))
                .build();
    }

    public Nutrient toEntity(UpdateNutrientDTO dto, Nutrient nutrient) {
        nutrient.setName(dto.getName());
        nutrient.setDescription(dto.getDescription());
        nutrient.setRdaValue(dto.getRdaValue());
        nutrient.setRdaUnit(dto.getRdaUnit());
        nutrient.setNutrientType(NutrientType.valueOf(dto.getNutrientType().toUpperCase()));
        nutrient.setUnit(dto.getUnit());

        return nutrient;
    }

    public ReadNutrientDto toDto(Nutrient nutrient) {
        return ReadNutrientDto.builder()
                .id(nutrient.getId())
                .name(nutrient.getName())
                .nutrientType(nutrient.getNutrientType().name())
                .unit(nutrient.getUnit())
                .description(nutrient.getDescription())
                .rdaValue(nutrient.getRdaValue())
                .rdaUnit(nutrient.getRdaUnit())
                .build();
    }

    public NutrientDto toSummary(Nutrient nutrient) {
        return NutrientDto.builder()
                .id(nutrient.getId())
                .name(nutrient.getName())
                .nutrientType(nutrient.getNutrientType().name())
                .build();
    }
}
