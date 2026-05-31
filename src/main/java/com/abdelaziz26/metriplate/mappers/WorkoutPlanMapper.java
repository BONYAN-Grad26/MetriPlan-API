package com.abdelaziz26.metriplate.mappers;

import com.abdelaziz26.metriplate.dtos.workout.ExerciseDto;
import com.abdelaziz26.metriplate.dtos.workout.WorkoutDayDto;
import com.abdelaziz26.metriplate.dtos.workout.WorkoutPlanResponseDto;
import com.abdelaziz26.metriplate.entities.user.User;
import com.abdelaziz26.metriplate.entities.workout.Exercise;
import com.abdelaziz26.metriplate.entities.workout.WorkoutDay;
import com.abdelaziz26.metriplate.entities.workout.WorkoutPlan;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

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

    public WorkoutPlanResponseDto toDto(WorkoutPlan workoutPlan) {
        if (workoutPlan == null) return null;

        Map<String, WorkoutDayDto> schedule = (workoutPlan.getDays() == null) ?
                new LinkedHashMap<>() :
                workoutPlan.getDays()
                        .stream()
                        .collect(Collectors.toMap(
                                WorkoutDay::getDayName,
                                day -> {
                                    List<ExerciseDto> exercises = (day.getExercises() == null) ?
                                            List.of() :
                                            day.getExercises()
                                                    .stream()
                                                    .map(e -> ExerciseDto.builder()
                                                            .name(e.getName())
                                                            .sets(e.getSets() == null ? 0 : e.getSets())
                                                            .reps(e.getReps())
                                                            .rest_seconds(e.getRestSeconds() == null ? 0 : e.getRestSeconds())
                                                            .notes(e.getNotes())
                                                            .build())
                                                    .collect(Collectors.toList());

                                    return WorkoutDayDto.builder()
                                            .session(day.getSession())
                                            .focus(day.getFocus())
                                            .exercises(exercises)
                                            .build();
                                },
                                (a, b) -> a,
                                LinkedHashMap::new
                        ));

        WorkoutPlanResponseDto dto = new WorkoutPlanResponseDto();
        dto.setPlan_name(workoutPlan.getPlanName());
        dto.setSplit_type(workoutPlan.getSplitType());
        dto.setSplit_reasoning(workoutPlan.getSplitReasoning());
        dto.setWeekly_schedule(schedule);
        return dto;
    }
    
    public WorkoutDayDto toDayDto(WorkoutDay day) {
        if (day == null) return null;

        List<ExerciseDto> exercises = (day.getExercises() == null) ?
                List.of() :
                day.getExercises()
                        .stream()
                        .map(e -> ExerciseDto.builder()
                                .name(e.getName())
                                .sets(e.getSets() == null ? 0 : e.getSets())
                                .reps(e.getReps())
                                .rest_seconds(e.getRestSeconds() == null ? 0 : e.getRestSeconds())
                                .notes(e.getNotes())
                                .build())
                        .collect(Collectors.toList());

        return WorkoutDayDto.builder()
                .session(day.getSession())
                .focus(day.getFocus())
                .exercises(exercises)
                .build();
    } 
}
