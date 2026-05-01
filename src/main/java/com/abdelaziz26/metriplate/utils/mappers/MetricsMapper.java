package com.abdelaziz26.metriplate.utils.mappers;

import com.abdelaziz26.metriplate.dtos.metrics.CreateHealthMetricDto;
import com.abdelaziz26.metriplate.dtos.metrics.NutritionCalcResult;
import com.abdelaziz26.metriplate.dtos.metrics.ReadHealthMetricDto;
import com.abdelaziz26.metriplate.dtos.metrics.UpdateHealthMetricDto;
import com.abdelaziz26.metriplate.entities.HealthMetrics;
import org.springframework.stereotype.Service;

@Service
public class MetricsMapper {

    public HealthMetrics toEntity(
            CreateHealthMetricDto dto,
            NutritionCalcResult calc
    ) {
        HealthMetrics entity = new HealthMetrics();

        entity.setAge(dto.getAge());
        entity.setWeightKg(dto.getWeightKg());
        entity.setHeightCm(dto.getHeightCm());
        entity.setGender(dto.getGender());
        entity.setActivityLevel(dto.getActivityLevel());
        entity.setDietGoal(dto.getDietGoal());
        entity.setDietType(dto.getDietType());

        entity.setFatPercentage(dto.getFatPercentage());
        entity.setMuscleMassKg(dto.getMuscleMassKg());

        entity.setBmi(calc.getBmi());
        entity.setTdee(calc.getTdee());

        entity.setDailyCalorieTarget(
                dto.getDailyCalorieTarget() != null
                        ? dto.getDailyCalorieTarget()
                        : calc.getCalorieTarget()
        );

        entity.setFatMass(
                calc.getFatMass() != null
                        ? calc.getFatMass()
                        : null
        );

        entity.setLeanMass(
                calc.getLeanMass() != null
                        ? calc.getLeanMass()
                        : null
        );

        return entity;
    }

    public HealthMetrics toEntity(
            UpdateHealthMetricDto dto,
            HealthMetrics entity,
            NutritionCalcResult calc
    ) {

        if (dto.getAge() != null) entity.setAge(dto.getAge());
        if (dto.getWeightKg() != null) entity.setWeightKg(dto.getWeightKg());
        if (dto.getHeightCm() != null) entity.setHeightCm(dto.getHeightCm());
        if (dto.getMuscleMassKg() != null) entity.setMuscleMassKg(dto.getMuscleMassKg());
        if (dto.getFatPercentage() != null) entity.setFatPercentage(dto.getFatPercentage());

        if (dto.getGender() != null) entity.setGender(dto.getGender());
        if (dto.getActivityLevel() != null) entity.setActivityLevel(dto.getActivityLevel());

        if (dto.getMedicalNotes() != null) entity.setMedicalNotes(dto.getMedicalNotes());
        if (dto.getDietType() != null) entity.setDietType(dto.getDietType());
        if (dto.getDietGoal() != null) entity.setDietGoal(dto.getDietGoal());

        if (dto.getTargetWeightKg() != null) entity.setTargetWeightKg(dto.getTargetWeightKg());

        if (dto.getDailyCalorieTarget() != null) {
            entity.setDailyCalorieTarget(dto.getDailyCalorieTarget());
        } else if (calc != null) {
            entity.setDailyCalorieTarget(calc.getCalorieTarget());
        }

        if (calc != null) {
            entity.setBmi(calc.getBmi());
            entity.setBmiCategory(calc.getBmiCategory());
            entity.setTdee(calc.getTdee());
            entity.setFatMass(calc.getFatMass());
            entity.setLeanMass(calc.getLeanMass());
            entity.setBodyFatCategory(calc.getBodyFatCategory());
        }

        return entity;
    }


    public ReadHealthMetricDto toReadDto(HealthMetrics entity) {
        ReadHealthMetricDto dto = new ReadHealthMetricDto();
        
        // Basic fields
        dto.setId(entity.getId());
        dto.setAge(entity.getAge());
        dto.setWeightKg(entity.getWeightKg());
        dto.setHeightCm(entity.getHeightCm());
        dto.setMuscleMassKg(entity.getMuscleMassKg());
        dto.setFatPercentage(entity.getFatPercentage());
        
        // Enums
        dto.setGender(entity.getGender());
        dto.setActivityLevel(entity.getActivityLevel());
        dto.setDietType(entity.getDietType());
        dto.setDietGoal(entity.getDietGoal());
        
        // Medical info
        dto.setMedicalNotes(entity.getMedicalNotes());
        
        // Calculated metrics
        dto.setBmi(entity.getBmi());
        dto.setBmiCategory(entity.getBmiCategory());
        dto.setTdee(entity.getTdee());
        dto.setFatMass(entity.getFatMass());
        dto.setLeanMass(entity.getLeanMass());
        dto.setBodyFatCategory(entity.getBodyFatCategory());

        // Goals
        dto.setTargetWeightKg(entity.getTargetWeightKg());
        dto.setDailyCalorieTarget(entity.getDailyCalorieTarget());
        
        return dto;
    }
}
