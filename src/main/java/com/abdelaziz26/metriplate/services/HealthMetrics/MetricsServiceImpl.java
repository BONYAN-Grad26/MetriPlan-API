package com.abdelaziz26.metriplate.services.HealthMetrics;

import com.abdelaziz26.metriplate.dtos.metrics.CreateHealthMetricDto;
import com.abdelaziz26.metriplate.dtos.metrics.ReadHealthMetricDto;
import com.abdelaziz26.metriplate.dtos.metrics.UpdateHealthMetricDto;
import com.abdelaziz26.metriplate.entities.HealthMetrics;
import com.abdelaziz26.metriplate.entities.User;
import com.abdelaziz26.metriplate.repositories.MetricsRepository;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Errors;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.security.SecurityContextService;
import com.abdelaziz26.metriplate.utils.mappers.MetricsMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("MetricsService")
@RequiredArgsConstructor
public class MetricsServiceImpl implements MetricsService {

    private final MetricsRepository metricsRepository;
    private final MetricsMapper metricsMapper;
    private final SecurityContextService securityContextService;

    @Override
    @PreAuthorize("@MetricsService.isOwner(#metricsId)")
    public Result<ReadHealthMetricDto, Error> getHealthMetricsById(Long metricsId) {

        return metricsRepository.findById(metricsId).map(m ->
                Result.CreateSuccessResult( metricsMapper.toDto(m) ) )
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
                Result.CreateSuccessResult( metricsMapper.toDto(m) ) )
                .orElseGet( () ->
                        Result.CreateErrorResult( Errors.NotFoundErr("No HealthMetric Associated to this Id")
                        )
                );
    }

    @Override
    public Result<ReadHealthMetricDto, Error> addHealthMetrics(CreateHealthMetricDto createHealthMetricDto) {
        Optional<User> user = securityContextService.getCurrentUser();

        if(user.isEmpty()) {
            return Result.CreateErrorResult(Errors.UnauthorizedErr("You are not authorized!"));
        }

        HealthMetrics healthMetrics = metricsMapper.toEntity(createHealthMetricDto);
        healthMetrics.setUser(user.get());

        HealthMetrics savedMetrics = metricsRepository.save(healthMetrics);

        ReadHealthMetricDto readHealthMetricDto = metricsMapper.toDto(savedMetrics);

        return Result.CreateSuccessResult(readHealthMetricDto);
    }

    @Transactional
    @PreAuthorize("@MetricsService.isOwner(#metricId)")
    @Override
    public Result<ReadHealthMetricDto, Error> updateHealthMetrics(Long metricId, UpdateHealthMetricDto updateHealthMetricDto) {

        Optional<HealthMetrics> metrics = metricsRepository.findById(metricId);

        if(metrics.isEmpty()) {
            return Result.CreateErrorResult(Errors.NotFoundErr("No HealthMetric Associated to this Id"));
        }

        HealthMetrics healthMetrics = metrics.get();

        healthMetrics = metricsMapper.toEntity(updateHealthMetricDto, healthMetrics);

        HealthMetrics updatedMetrics = metricsRepository.save(healthMetrics);

        ReadHealthMetricDto readHealthMetricDto = metricsMapper.toDto(updatedMetrics);

        return Result.CreateSuccessResult(readHealthMetricDto);
    }

    @PreAuthorize("@MetricsService.isOwner(#metricsId)")
    @Override
    public Result<String, Error> deleteHealthMetrics(Long metricsId) {
        metricsRepository.deleteById(metricsId);
        return Result.CreateSuccessResult("HealthMetrics deleted successfully");
    }


    public boolean isOwner(Long metricsId) {
        User user = securityContextService.getCurrentUser().orElse(null);

        if(user == null) {
            return false;
        }

        return metricsRepository.existsByIdAndUserId(metricsId, user.getId());
    }
}
