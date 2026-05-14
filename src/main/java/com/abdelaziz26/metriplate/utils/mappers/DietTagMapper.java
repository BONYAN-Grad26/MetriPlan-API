package com.abdelaziz26.metriplate.utils.mappers;

import com.abdelaziz26.metriplate.dtos.tag.CreateDietaryTagDto;
import com.abdelaziz26.metriplate.dtos.tag.DietaryTagDto;
import com.abdelaziz26.metriplate.dtos.tag.ReadDietaryTagDto;
import com.abdelaziz26.metriplate.dtos.tag.UpdateDietaryTagDto;
import com.abdelaziz26.metriplate.entities.diet.DietaryTag;
import com.abdelaziz26.metriplate.enums.DietaryTagType;
import org.springframework.stereotype.Service;

@Service
public class DietTagMapper {
    public DietaryTag toEntity(CreateDietaryTagDto dto){
        return DietaryTag.builder()
                .type(DietaryTagType.valueOf(dto.getType()))
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public DietaryTag toEntity(UpdateDietaryTagDto dto, DietaryTag tag){

        if(dto.getType() != null)
          tag.setType(DietaryTagType.valueOf(dto.getType()));
        if(dto.getName() != null)
          tag.setName(dto.getName());
        if(dto.getDescription() != null)
          tag.setDescription(dto.getDescription());

        return tag;
    }

    public ReadDietaryTagDto toDto(DietaryTag dietaryTag){
        return ReadDietaryTagDto.builder()
                .id(dietaryTag.getId())
                .type(dietaryTag.getType().name())
                .name(dietaryTag.getName())
                .description(dietaryTag.getDescription())
                .build();
    }

    public DietaryTagDto toSummary(DietaryTag dietaryTag){
        return DietaryTagDto.builder()
                .id(dietaryTag.getId())
                .name(dietaryTag.getName())
                .build();
    }
}
