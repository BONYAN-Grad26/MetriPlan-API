package com.abdelaziz26.metriplate.utils.mappers;

import com.abdelaziz26.metriplate.dtos.metrics.CreateHealthMetricDto;
import com.abdelaziz26.metriplate.dtos.metrics.ReadHealthMetricDto;
import com.abdelaziz26.metriplate.dtos.metrics.UpdateHealthMetricDto;
import com.abdelaziz26.metriplate.entities.HealthMetrics;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MetricsMapper {

    public HealthMetrics toEntity(CreateHealthMetricDto dto) {
        return HealthMetrics.builder()
                .notes(dto.getNotes())
                .recordedAt(LocalDate.now())
                .bmi(dto.getBmi())
                .muscleMass(dto.getMuscleMass())
                .bodyFatPercentage(dto.getBodyFatPercentage())
                .hipCircumference(dto.getHipCircumference())
                .waistCircumference(dto.getWaistCircumference())
                .build();
    }

    public ReadHealthMetricDto toDto(HealthMetrics entity) {
        return ReadHealthMetricDto.builder()
                .id(entity.getId())
                .notes(entity.getNotes())
                .recordedAt(entity.getRecordedAt())
                .bmi(entity.getBmi())
                .muscleMass(entity.getMuscleMass())
                .bodyFatPercentage(entity.getBodyFatPercentage())
                .hipCircumference(entity.getHipCircumference())
                .waistCircumference(entity.getWaistCircumference())
                .bmiCategory(null) // logic needed here
                .build();
    }

    public HealthMetrics toEntity(UpdateHealthMetricDto dto, HealthMetrics entity) {
        entity.setNotes(dto.getNotes());
        entity.setRecordedAt(LocalDate.now());
        entity.setBmi(dto.getBmi());
        entity.setMuscleMass(dto.getMuscleMass());
        entity.setBodyFatPercentage(dto.getBodyFatPercentage());
        entity.setHipCircumference(dto.getHipCircumference());
        entity.setWaistCircumference(dto.getWaistCircumference());
        return entity;
    }
}
