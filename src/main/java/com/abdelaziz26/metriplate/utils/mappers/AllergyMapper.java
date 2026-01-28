package com.abdelaziz26.metriplate.utils.mappers;

import com.abdelaziz26.metriplate.dtos.allergy.CreateAllergyDto;
import com.abdelaziz26.metriplate.dtos.allergy.ReadAllergyDto;
import com.abdelaziz26.metriplate.dtos.allergy.UpdateAllergyDto;
import com.abdelaziz26.metriplate.entities.Allergy;
import com.abdelaziz26.metriplate.entities.Nutrient;
import com.abdelaziz26.metriplate.entities.User;
import com.abdelaziz26.metriplate.enums.AllergenType;
import org.springframework.stereotype.Service;

@Service
public class AllergyMapper {

    public Allergy toEntity(CreateAllergyDto dto, Nutrient nutrient, User user) {
        return Allergy.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .type(AllergenType.valueOf(dto.getType()))
                .nutrient(nutrient)
                .user(user)
                .build();
    }

    public ReadAllergyDto toDto(Allergy allergy) {
        return ReadAllergyDto.builder()
                .id(allergy.getId())
                .name(allergy.getName())
                .description(allergy.getDescription())
                .type(allergy.getType().name())
                .nutrientName(allergy.getNutrient().getName())
                .build();
    }

    public Allergy toEntity(UpdateAllergyDto dto, Allergy allergy) {
        allergy.setName(dto.getName());
        allergy.setDescription(dto.getDescription());
        allergy.setType(AllergenType.valueOf(dto.getType()));
        return allergy;
    }
}
