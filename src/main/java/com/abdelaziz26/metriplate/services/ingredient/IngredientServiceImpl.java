package com.abdelaziz26.metriplate.services.ingredient;

import com.abdelaziz26.metriplate.dtos.ingredients.CreateIngredientDto;
import com.abdelaziz26.metriplate.dtos.ingredients.IngredientDto;
import com.abdelaziz26.metriplate.dtos.ingredients.ReadIngredientDto;
import com.abdelaziz26.metriplate.dtos.ingredients.UpdateIngredientDto;
import com.abdelaziz26.metriplate.entities.diet.DietaryTag;
import com.abdelaziz26.metriplate.entities.diet.Ingredient;
import com.abdelaziz26.metriplate.enums.DietaryTagType;
import com.abdelaziz26.metriplate.repositories.IngredientRepository;
import com.abdelaziz26.metriplate.repositories.TagRepository;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Errors;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.utils.mappers.IngredientMapper;
import com.abdelaziz26.metriplate.utils.specifications.IngredientSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("IngredientService")
@RequiredArgsConstructor
@Transactional
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;
    private final TagRepository tagRepository;

    @Override
    public Result<ReadIngredientDto, Error> getById(long id) {
        return ingredientRepository.findById(id)
                .map(i ->
                        Result.CreateSuccessResult(ingredientMapper.toDto(i))
                )
                .orElse(Result.CreateErrorResult(Errors.NotFoundErr("Ingredient with id " + id + " not found") )
                );
    }

    @Override
    public Result<ReadIngredientDto, Error> getByName(String name) {
        return ingredientRepository.findByNameLike(name)
                .map(i ->
                        Result.CreateSuccessResult(ingredientMapper.toDto(i))
                )
                .orElse(Result.CreateErrorResult(Errors.NotFoundErr("Ingredient with name " + name + " not found") )
                );
    }

    @Override
    public Result<List<IngredientDto>, Error> getAll(int pageIdx) {

        if (pageIdx < 1) {
            return Result.CreateErrorResult(Errors.BadRequestErr("Page index must be >= 1"));
        }
        Pageable pageable = PageRequest.of(pageIdx - 1, 10);
        return Result.CreateSuccessResult(ingredientRepository.findAll(pageable).stream()
                .map(ingredientMapper::toSummary)
                .collect(Collectors.toList())
        );
    }

    @Override
    public Result<List<IngredientDto>, Error> getAll(int pageIdx, List<DietaryTagType> dietaryTagTypes) {
        Specification<@NotNull Ingredient> specs = IngredientSpecifications.hasDietaryTagType(dietaryTagTypes);
        if (pageIdx < 1) {
            return Result.CreateErrorResult(Errors.BadRequestErr("Page index must be >= 1"));
        }

        if (dietaryTagTypes == null || dietaryTagTypes.isEmpty()) {
            return getAll(pageIdx);
        }

        Pageable pageable = PageRequest.of(pageIdx - 1, 10);
        return Result.CreateSuccessResult(ingredientRepository.findAll(specs, pageable).stream()
                .map(ingredientMapper::toSummary)
                .collect(Collectors.toList())
        );
    }


    @Override
    public Result<ReadIngredientDto, Error> addIngredient(CreateIngredientDto dto) {
        boolean exists = ingredientRepository.existsByName(dto.getName());

        if(exists) {
            return Result.CreateErrorResult(Errors.BadRequestErr("An ingredient already exists with the name " + dto.getName()));
        }

        List<DietaryTag> dietaryTags = tagRepository.findAllById(dto.getDietaryTagIds());

        return Result.CreateSuccessResult( ingredientMapper
                .toDto( ingredientRepository
                        .save(ingredientMapper.toEntity(dto, dietaryTags))
                )
        );
    }

    @Override
    public Result<ReadIngredientDto, Error> updateIngredient(Long id, UpdateIngredientDto dto) {
        return ingredientRepository.findById(id).map(i -> {
            i = ingredientMapper.toEntity(dto, i);
            return Result.CreateSuccessResult(ingredientMapper.toDto(ingredientRepository.save(i)));
        }).orElse(Result.CreateErrorResult(Errors.NotFoundErr("Ingredient with id " + id + " not found")));
    }

    @Override
    public Result<String, Error> deleteIngredient(Long id) {
        boolean exists = ingredientRepository.existsById(id);
        if(!exists) {
            return Result.CreateErrorResult(Errors.NotFoundErr("Ingredient with id " + id + " not found"));
        }
        ingredientRepository.deleteById(id);
        return Result.CreateSuccessResult("Ingredient with id " + id + " deleted successfully");
    }

    @Override
    public Result<String, Error> addIngredientTag(Long ingredientId, List<Long> tagIds) {

        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);

        if(ingredient == null) {
            return Result.CreateErrorResult(Errors.NotFoundErr("Ingredient with id " + ingredientId + " not found"));
        }

        List<DietaryTag> tags = tagRepository.findAllById(tagIds);

        if(tags.size() != tagIds.size()) {
            return Result.CreateErrorResult(Errors.BadRequestErr("One or more tags not found"));
        }

        tags.forEach(t -> {
            if (! ingredient.getDietaryTags().contains(t)) {
                ingredient.getDietaryTags().add(t);
            }
        });

        // ingredient.getDietaryTags().addAll(tags);
        ingredientRepository.save(ingredient);

        return Result.CreateSuccessResult("Ingredient with id " + ingredientId + " assigned with tags successfully");
    }

    @Override
    public Result<String, Error> removeIngredientTag(Long ingredientId, List<Long> tagIds) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);

        if(ingredient == null) {
            return Result.CreateErrorResult(Errors.NotFoundErr("Ingredient with id " + ingredientId + " not found"));
        }

        List<DietaryTag> tags = tagRepository.findAllById(tagIds);

        if(tags.size() != tagIds.size()) {
            return Result.CreateErrorResult(Errors.BadRequestErr("One or more tags not found"));
        }

        ingredient.getDietaryTags().removeAll(tags);
        ingredientRepository.save(ingredient);

        return Result.CreateSuccessResult("tags de-assigned successfully");
    }

}
