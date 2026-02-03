package com.abdelaziz26.metriplate.services.tag;

import com.abdelaziz26.metriplate.dtos.tag.CreateDietaryTagDto;
import com.abdelaziz26.metriplate.dtos.tag.DietaryTagDto;
import com.abdelaziz26.metriplate.dtos.tag.ReadDietaryTagDto;
import com.abdelaziz26.metriplate.dtos.tag.UpdateDietaryTagDto;
import com.abdelaziz26.metriplate.entities.DietaryTag;
import com.abdelaziz26.metriplate.repositories.TagRepository;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Errors;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.utils.mappers.DietTagMapper;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("TagService")
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final DietTagMapper tagMapper;

    @Override
    public Result<ReadDietaryTagDto, Error> getById(Long id) {
        return tagRepository.findById(id).map(t ->
                Result.CreateSuccessResult(tagMapper.toDto(t))
        ).orElse(
                Result.CreateErrorResult(Errors.NotFoundErr("Tag Not Found"))
        );
    }

    @Override
    public Result<ReadDietaryTagDto, Error> getByName(String name) {
        return tagRepository.findByName(name).map(t ->
                Result.CreateSuccessResult(tagMapper.toDto(t))
        ).orElse(
                Result.CreateErrorResult(Errors.NotFoundErr("Tag Not Found"))
        );
    }

    @Override
    public Result<List<DietaryTagDto>, Error> getAll(int pageIdx) {

        if (pageIdx < 1) {
            return Result.CreateErrorResult(Errors.BadRequestErr("Page index must be >= 1"));
        }

        Page<@NonNull DietaryTag> page = tagRepository.findAll(PageRequest.of(pageIdx - 1, 10));
        return Result.CreateSuccessResult(page.stream().map(tagMapper::toSummary)
                .collect(Collectors.toList())
        );
    }

    @Override
    public Result<ReadDietaryTagDto, Error> addTag(CreateDietaryTagDto dto) {

        boolean exists = tagRepository.existsByName(dto.getName());

        if(exists) {
            return Result.CreateErrorResult(Errors.BadRequestErr("The tag already exists"));
        }

        DietaryTag tag =  tagMapper.toEntity(dto);
        tag = tagRepository.save(tag);

        return Result.CreateSuccessResult(tagMapper.toDto(tag));
    }

    @Transactional
    @Override
    public Result<ReadDietaryTagDto, Error> updateTag(Long id, UpdateDietaryTagDto dto) {

        DietaryTag tag = tagRepository.findById(id).orElse(null);

        if(tag == null)
            return Result.CreateErrorResult(Errors.NotFoundErr("Tag Not Found"));

        if (!tag.getName().equals(dto.getName()) && tagRepository.existsByName(dto.getName())) {
            return Result.CreateErrorResult(Errors.BadRequestErr("Tag name already exists"));
        }

        try {
            tag = tagRepository.save(tagMapper.toEntity(dto, tag));
            return Result.CreateSuccessResult(tagMapper.toDto(tag));
        } catch (IllegalArgumentException e) {
            return Result.CreateErrorResult(Errors.BadRequestErr("Invalid tag type"));
        }
    }

    @Transactional
    @Override
    public Result<String, Error> deleteTag(Long id) {
        boolean exists = tagRepository.existsById(id);
        if(!exists) {
            return Result.CreateErrorResult(Errors.NotFoundErr("Tag Not Found"));
        }

        tagRepository.deleteById(id);

        return Result.CreateSuccessResult("Tag Deleted");
    }
}
