package com.abdelaziz26.metriplate.services.HealthMetrics;

import com.abdelaziz26.metriplate.dtos.metrics.CreateHealthMetricDto;
import com.abdelaziz26.metriplate.dtos.metrics.ReadHealthMetricDto;
import com.abdelaziz26.metriplate.dtos.metrics.UpdateHealthMetricDto;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;

public interface MetricsService {
    Result<ReadHealthMetricDto, Error> getHealthMetricsById(Long metricsId);
    Result<ReadHealthMetricDto, Error> getHealthMetricsByUserId(); // currently authenticated

    Result<ReadHealthMetricDto, Error> addHealthMetrics(CreateHealthMetricDto createHealthMetricDto);
    Result<ReadHealthMetricDto, Error> updateHealthMetrics(Long metricId, UpdateHealthMetricDto updateHealthMetricDto);

    Result<String, Error> deleteHealthMetrics(Long metricsId);

}
