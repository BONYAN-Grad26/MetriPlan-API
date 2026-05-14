package com.abdelaziz26.metriplate.services.allergy;

import com.abdelaziz26.metriplate.dtos.allergy.CreateAllergyDto;
import com.abdelaziz26.metriplate.dtos.allergy.ReadAllergyDto;
import com.abdelaziz26.metriplate.dtos.allergy.UpdateAllergyDto;
import com.abdelaziz26.metriplate.entities.user.Allergy;
import com.abdelaziz26.metriplate.entities.user.User;
import com.abdelaziz26.metriplate.repositories.AllergyRepository;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Errors;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.security.SecurityContextService;
import com.abdelaziz26.metriplate.utils.mappers.AllergyMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("AllergyService")
@RequiredArgsConstructor
public class AllergyServiceImpl implements AllergyService {

    private final AllergyRepository allergyRepository;
    private final AllergyMapper allergyMapper;
    private final SecurityContextService securityContextService;

    @Override
    public Result<ReadAllergyDto, Error> getById(Long id) {
        return allergyRepository.findById(id)
                .map(a ->
                        Result.CreateSuccessResult( allergyMapper.toDto(a) )
                )
                .orElse(Result.CreateErrorResult(
                        Errors.NotFoundErr("Allergy Not Found")
                ));
    }

    @Override
    public Result<List<ReadAllergyDto>, Error> getByUserId(Long userId) {
        return Result.CreateSuccessResult(
                allergyRepository.findByUser_Id(userId)
                        .stream()
                        .map(allergyMapper::toDto)
                        .toList()
        );
    }

    @Transactional
    @Override
    public Result<ReadAllergyDto, Error> addAllergy(CreateAllergyDto dto) {
        User user = securityContextService.getCurrentUser().orElse(null);

        if (user == null)
            return Result.CreateErrorResult(
                    Errors.NotFoundErr("User Not Found")
            );

        Allergy allergy = allergyMapper.toEntity(dto, user);

        return Result.CreateSuccessResult(
                allergyMapper.toDto(allergyRepository.save(allergy))
        );
    }

    @Transactional
    @PreAuthorize("@AllergyService.isOwner(#id)")
    @Override
    public Result<ReadAllergyDto, Error> updateAllergy(Long id, UpdateAllergyDto dto) {
        Allergy allergy = allergyRepository.findById(id).orElse(null);
        if (allergy == null)
            return Result.CreateErrorResult(
                    Errors.NotFoundErr("Allergy Not Found")
            );
        allergyMapper.toEntity(dto, allergy);
        return Result.CreateSuccessResult(
                allergyMapper.toDto(allergyRepository.save(allergy))
        );
    }

    @Transactional
    @PreAuthorize("@AllergyService.isOwner(#id)")
    @Override
    public Result<String, Error> deleteAllergy(Long id) {
        allergyRepository.deleteById(id);
        return Result.CreateSuccessResult("Allergy deleted successfully.");
    }

    public boolean isOwner(Long id) {
        User user = securityContextService.getCurrentUser().orElse(null);
        if (user == null)
            return false;
        return allergyRepository.existsByIdAndUser_Id(id, user.getId());
    }
}
