package com.abdelaziz26.metriplate.utils.mappers;

import com.abdelaziz26.metriplate.dtos.plan.DayDTO;
import com.abdelaziz26.metriplate.entities.DailyPlan;
import com.abdelaziz26.metriplate.entities.WeeklyPlan;
import com.abdelaziz26.metriplate.enums.DayStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DailyPlanMapper {
    public DailyPlan toEntity(DayDTO dto, WeeklyPlan weeklyPlan) {
        if (dto == null) return null;

        return DailyPlan.builder()
                .weeklyPlan(weeklyPlan)
                .date(parseDate(dto.getDate()))
                .dayOfWeek(dto.getDayOfWeek())
                .targetCalories(dto.getTargetCalories())
                .targetProtein(dto.getTargetProtein())
                .targetCarbs(dto.getTargetCarbs())
                .targetFat(dto.getTargetFat())
                .targetFiber(dto.getTargetFiber())
                .targetSugar(dto.getTargetSugar())
                .waterGoal(dto.getWaterGoal())
                .aiDailyTips(dto.getAiDailyTips())
                .status(DayStatus.PLANNED)
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
