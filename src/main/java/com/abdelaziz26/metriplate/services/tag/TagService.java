package com.abdelaziz26.metriplate.services.tag;

import com.abdelaziz26.metriplate.dtos.tag.CreateDietaryTagDto;
import com.abdelaziz26.metriplate.dtos.tag.DietaryTagDto;
import com.abdelaziz26.metriplate.dtos.tag.ReadDietaryTagDto;
import com.abdelaziz26.metriplate.dtos.tag.UpdateDietaryTagDto;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;

import java.util.List;

public interface TagService {
    Result<ReadDietaryTagDto, Error> getById(Long id);
    Result<ReadDietaryTagDto, Error> getByName(String name);
    Result<List<DietaryTagDto>, Error> getAll(int pageIdx);

    Result<ReadDietaryTagDto, Error> addTag(CreateDietaryTagDto dto);
    Result<ReadDietaryTagDto, Error> updateTag(Long id, UpdateDietaryTagDto dto);

    Result<String, Error>  deleteTag(Long id);
}
