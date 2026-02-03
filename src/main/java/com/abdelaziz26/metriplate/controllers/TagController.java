package com.abdelaziz26.metriplate.controllers;

import com.abdelaziz26.metriplate.dtos.tag.CreateDietaryTagDto;
import com.abdelaziz26.metriplate.dtos.tag.DietaryTagDto;
import com.abdelaziz26.metriplate.dtos.tag.ReadDietaryTagDto;
import com.abdelaziz26.metriplate.dtos.tag.UpdateDietaryTagDto;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.services.tag.TagService;
import com.abdelaziz26.metriplate.utils._Abdel3zizController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController extends _Abdel3zizController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<@NotNull Result<List<DietaryTagDto>, Error>> getAllTags(int pageIdx) {
        Result<List<DietaryTagDto>, Error> result = tagService.getAll(pageIdx);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NotNull Result<ReadDietaryTagDto, Error>> getTagById(@PathVariable Long id) {
        Result<ReadDietaryTagDto, Error> result = tagService.getById(id);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @PostMapping
    public ResponseEntity<@NotNull Result<ReadDietaryTagDto, Error>> createTag(@Valid @RequestBody CreateDietaryTagDto createDto) {
        Result<ReadDietaryTagDto, Error> result = tagService.addTag(createDto);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<@NotNull Result<ReadDietaryTagDto, Error>> updateTag(@PathVariable Long id, @Valid @RequestBody UpdateDietaryTagDto updateDto) {
        Result<ReadDietaryTagDto, Error> result = tagService.updateTag(id, updateDto);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<@NotNull Result<String, Error>> deleteTag(@PathVariable Long id) {
        Result<String, Error> result = tagService.deleteTag(id);
        return new ResponseEntity<>(result, resolveStatus(result));
    }
}