package com.abdelaziz26.metriplate.utils.mappers;

import com.abdelaziz26.metriplate.dtos.plan.WeekDTO;
import com.abdelaziz26.metriplate.entities.DietPlan;
import com.abdelaziz26.metriplate.entities.WeeklyPlan;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class WeeklyPlanMapper {
    public WeeklyPlan toEntity(WeekDTO dto, DietPlan dietPlan) {
        if (dto == null) return null;

        return WeeklyPlan.builder()
                .dietPlan(dietPlan)
                .weekNumber(dto.getWeekNumber())
                .startDate(parseDate(dto.getStartDate()))
                .endDate(parseDate(dto.getEndDate()))
                .weeklyCalorieTarget(dto.getWeeklyCalorieTarget())
                .weeklyProteinTarget(dto.getWeeklyProteinTarget())
                .weeklyCarbTarget(dto.getWeeklyCarbTarget())
                .weeklyFatTarget(dto.getWeeklyFatTarget())
                .weeklyStrategy(dto.getWeeklyStrategy())
                .aiPreparationTips(dto.getAiPreparationTips())
                .build();
    }

    private LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            return LocalDate.now();
        }
    }
}
