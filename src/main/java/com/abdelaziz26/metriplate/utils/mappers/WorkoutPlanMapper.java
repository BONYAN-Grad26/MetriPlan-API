package com.abdelaziz26.metriplate.utils.mappers;

import com.abdelaziz26.metriplate.dtos.workout.ExerciseDto;
import com.abdelaziz26.metriplate.dtos.workout.WorkoutDayDto;
import com.abdelaziz26.metriplate.dtos.workout.WorkoutPlanResponseDto;
import com.abdelaziz26.metriplate.entities.user.User;
import com.abdelaziz26.metriplate.entities.workout.Exercise;
import com.abdelaziz26.metriplate.entities.workout.WorkoutDay;
import com.abdelaziz26.metriplate.entities.workout.WorkoutPlan;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkoutPlanMapper {
    public WorkoutPlan toEntity(WorkoutPlanResponseDto dto, User user) {
        WorkoutPlan plan = new WorkoutPlan();
        plan.setPlanName(dto.getPlan_name());
        plan.setSplitType(dto.getSplit_type());
        plan.setSplitReasoning(dto.getSplit_reasoning());
        plan.setUser(user);

        List<WorkoutDay> days = dto.getWeekly_schedule()
                .entrySet()
                .stream()
                .map(entry -> mapDay(entry.getKey(), entry.getValue(), plan))
                .toList();

        plan.setDays(days);
        return plan;
    }

    private WorkoutDay mapDay(String dayName, WorkoutDayDto dto, WorkoutPlan plan) {
        WorkoutDay day = new WorkoutDay();
        day.setDayName(dayName);
        day.setSession(dto.getSession());
        day.setFocus(dto.getFocus());
        day.setPlan(plan);

        List<Exercise> exercises = dto.getExercises()
                .stream()
                .map(e -> mapExercise(e, day))
                .toList();

        day.setExercises(exercises);
        return day;
    }

    private Exercise mapExercise(ExerciseDto dto, WorkoutDay day) {
        Exercise ex = new Exercise();
        ex.setName(dto.getName());
        ex.setSets(dto.getSets());
        ex.setReps(dto.getReps());
        ex.setRestSeconds(dto.getRest_seconds());
        ex.setNotes(dto.getNotes());
        ex.setDay(day);
        return ex;
    }
}
