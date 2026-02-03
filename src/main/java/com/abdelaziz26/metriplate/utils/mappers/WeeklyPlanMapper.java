package com.abdelaziz26.metriplate.utils.mappers;

import com.abdelaziz26.metriplate.dtos.plan.WeekDTO;
import com.abdelaziz26.metriplate.entities.DietPlan;
import com.abdelaziz26.metriplate.entities.WeeklyPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WeeklyPlanMapper {

    private final DailyPlanMapper dailyPlanMapper;

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


    public WeekDTO toDto(WeeklyPlan entity) {
        if (entity == null) return null;

        WeekDTO dto = new WeekDTO();
        dto.setWeekNumber(entity.getWeekNumber());
        dto.setStartDate(entity.getStartDate().toString());
        dto.setEndDate(entity.getEndDate().toString());
        dto.setWeeklyCalorieTarget(entity.getWeeklyCalorieTarget());
        dto.setWeeklyProteinTarget(entity.getWeeklyProteinTarget());
        dto.setWeeklyCarbTarget(entity.getWeeklyCarbTarget());
        dto.setWeeklyFatTarget(entity.getWeeklyFatTarget());
        dto.setWeeklyStrategy(entity.getWeeklyStrategy());
        dto.setAiPreparationTips(entity.getAiPreparationTips());
        dto.setDays(entity.getDailyPlans().stream().map(dailyPlanMapper::toDto).collect(Collectors.toList()));

        return dto;
    }


}
