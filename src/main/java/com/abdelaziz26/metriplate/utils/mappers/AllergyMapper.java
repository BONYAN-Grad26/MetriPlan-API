package com.abdelaziz26.metriplate.utils.mappers;

import com.abdelaziz26.metriplate.dtos.allergy.CreateAllergyDto;
import com.abdelaziz26.metriplate.dtos.allergy.ReadAllergyDto;
import com.abdelaziz26.metriplate.dtos.allergy.UpdateAllergyDto;
import com.abdelaziz26.metriplate.entities.user.Allergy;
import com.abdelaziz26.metriplate.entities.user.User;
import com.abdelaziz26.metriplate.enums.AllergenType;
import org.springframework.stereotype.Service;

@Service
public class AllergyMapper {

    public Allergy toEntity(CreateAllergyDto dto, User user) {
        Allergy allergy = new Allergy();
        allergy.setName(dto.getName());
        allergy.setDescription(dto.getDescription());
        allergy.setType(AllergenType.valueOf(dto.getType()));
        allergy.setUser(user);
        return allergy;
    }

    public ReadAllergyDto toDto(Allergy allergy) {
        ReadAllergyDto dto = new ReadAllergyDto();
        dto.setId(allergy.getId());
        dto.setName(allergy.getName());
        dto.setDescription(allergy.getDescription());
        dto.setType(allergy.getType().name());
        dto.setUserEmail(allergy.getUser().getEmail());
        return dto;
    }

    public Allergy toEntity(UpdateAllergyDto dto, Allergy allergy) {
        allergy.setName(dto.getName());
        allergy.setDescription(dto.getDescription());
        allergy.setType(AllergenType.valueOf(dto.getType()));
        return allergy;
    }
}
