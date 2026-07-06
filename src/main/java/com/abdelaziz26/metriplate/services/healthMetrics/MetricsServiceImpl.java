package com.abdelaziz26.metriplate.services.healthMetrics;

import com.abdelaziz26.metriplate.dtos.metrics.CreateHealthMetricDto;
import com.abdelaziz26.metriplate.dtos.metrics.NutritionCalcResult;
import com.abdelaziz26.metriplate.dtos.metrics.ReadHealthMetricDto;
import com.abdelaziz26.metriplate.dtos.metrics.UpdateHealthMetricDto;
import com.abdelaziz26.metriplate.entities.user.HealthMetrics;
import com.abdelaziz26.metriplate.entities.user.User;
import com.abdelaziz26.metriplate.repositories.MetricsRepository;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Errors;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.security.SecurityContextService;
import com.abdelaziz26.metriplate.utils.NutritionCalculator;
import com.abdelaziz26.metriplate.mappers.MetricsMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service("MetricsService")
@RequiredArgsConstructor
public class MetricsServiceImpl implements MetricsService {

    private final MetricsRepository        metricsRepository;
    private final MetricsMapper            metricsMapper;
    private final SecurityContextService   securityContextService;

    public boolean isOwner(Long metricsId) {
        User user = securityContextService.getCurrentUser().orElse(null);

        if(user == null) {
            return false;
        }

        log.info("Checking ownership for metricsId: {} and userId: {}", metricsId, user.getId());

        return metricsRepository.existsByIdAndUserId(metricsId, user.getId());
    }

    @Override
    @PreAuthorize("@MetricsService.isOwner(#metricsId)")
    public Result<ReadHealthMetricDto, Error> getHealthMetricsById(Long metricsId) {

        return metricsRepository.findById(metricsId).map(m ->
                Result.CreateSuccessResult( metricsMapper.toReadDto(m) ) )
                .orElseGet( () ->
                        Result.CreateErrorResult( Errors.NotFoundErr("No HealthMetric Associated to this Id")
                        )
                );
    }

    @Override
    public Result<ReadHealthMetricDto, Error> getHealthMetricsByUserId() {

        User user = securityContextService.getCurrentUser().orElse(null);

        if(user == null) {
            return Result.CreateErrorResult(Errors.UnauthorizedErr("You are not authorized!"));
        }

        return metricsRepository.findByUser_Id(user.getId()).map(m ->
                Result.CreateSuccessResult( metricsMapper.toReadDto(m) ) )
                .orElseGet( () ->
                        Result.CreateErrorResult( Errors.NotFoundErr("No HealthMetric Associated to this Id")
                        )
                );
    }

    @Override
    public Result<ReadHealthMetricDto, Error> addHealthMetrics(CreateHealthMetricDto dto) {
        Optional<User> user = securityContextService.getCurrentUser();

        if(user.isEmpty()) {
            return Result.CreateErrorResult(Errors.UnauthorizedErr("You are not authorized!"));
        }

        NutritionCalcResult calcResult = NutritionCalculator.calculateAll(
                dto.getWeightKg(),
                dto.getHeightCm(),
                dto.getAge(),
                dto.getGender(),
                dto.getActivityLevel(),
                dto.getDietGoal(),
                dto.getFatPercentage());

        HealthMetrics healthMetrics = metricsMapper.toEntity(dto, calcResult);
        healthMetrics.setUser(user.get());

        HealthMetrics savedMetrics = metricsRepository.save(healthMetrics);

        ReadHealthMetricDto readHealthMetricDto = metricsMapper.toReadDto(savedMetrics);

        return Result.CreateSuccessResult(readHealthMetricDto);
    }

    @Transactional
    @PreAuthorize("@MetricsService.isOwner(#metricsId)")
    @Override
    public Result<ReadHealthMetricDto, Error> updateHealthMetrics(Long metricsId, UpdateHealthMetricDto dto) {

        Optional<HealthMetrics> metrics = metricsRepository.findById(metricsId);

        if(metrics.isEmpty()) {
            return Result.CreateErrorResult(Errors.NotFoundErr("No HealthMetric Associated to this Id"));
        }

        HealthMetrics healthMetrics = metrics.get();

        NutritionCalcResult calcResult = NutritionCalculator.calculateAll(
                dto.getWeightKg(),
                dto.getHeightCm(),
                dto.getAge(),
                dto.getGender(),
                dto.getActivityLevel(),
                dto.getDietGoal(),
                dto.getFatPercentage());

        healthMetrics = metricsMapper.toEntity(dto, healthMetrics, calcResult);

        HealthMetrics updatedMetrics = metricsRepository.save(healthMetrics);

        ReadHealthMetricDto readHealthMetricDto = metricsMapper.toReadDto(updatedMetrics);

        return Result.CreateSuccessResult(readHealthMetricDto);
    }

    @PreAuthorize("@MetricsService.isOwner(#metricsId)")
    @Override
    public Result<String, Error> deleteHealthMetrics(Long metricsId) {
        metricsRepository.deleteById(metricsId);
        return Result.CreateSuccessResult("HealthMetrics deleted successfully");
    }

}
