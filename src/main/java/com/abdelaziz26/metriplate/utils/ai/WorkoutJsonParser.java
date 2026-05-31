package com.abdelaziz26.metriplate.utils.ai;

import com.abdelaziz26.metriplate.dtos.workout.ExerciseDto;
import com.abdelaziz26.metriplate.dtos.workout.WorkoutDayDto;
import com.abdelaziz26.metriplate.dtos.workout.WorkoutPlanResponseDto;
import com.abdelaziz26.metriplate.exceptions.PlanGenerationException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class WorkoutJsonParser {

    private final ObjectMapper objectMapper;

    public WorkoutPlanResponseDto parseWorkoutPlanResponse(String json) throws Exception {
        String cleanJson = stripMarkdownFences(json);
        JsonNode root = objectMapper.readTree(cleanJson);

        if (root.has("error")) {
            throw new IllegalStateException("LLM returned error: " + root.path("error").asText());
        }

        WorkoutPlanResponseDto plan = new WorkoutPlanResponseDto();
        plan.setPlan_name(root.path("plan_name").asText());
        plan.setSplit_type(root.path("split_type").asText());
        plan.setSplit_reasoning(root.path("split_reasoning").asText());
        plan.setWeekly_schedule(buildWeeklySchedule(root.path("weekly_schedule")));

        return plan;
    }

    // -------------------------------------------------------------------------
    // Weekly Schedule
    // -------------------------------------------------------------------------

    private Map<String, WorkoutDayDto> buildWeeklySchedule(JsonNode scheduleNode) {
        Map<String, WorkoutDayDto> schedule = new LinkedHashMap<>(); // LinkedHashMap to preserve day order

        if (scheduleNode.isMissingNode() || !scheduleNode.isObject()) {
            return schedule;
        }

        scheduleNode.fields().forEachRemaining(entry ->
                schedule.put(entry.getKey(), buildWorkoutDayDto(entry.getValue()))
        );

        return schedule;
    }

    // -------------------------------------------------------------------------
    // Day
    // -------------------------------------------------------------------------

    private WorkoutDayDto buildWorkoutDayDto(JsonNode dayNode) {
        WorkoutDayDto day = new WorkoutDayDto();

        day.setSession(dayNode.path("session").asText());
        day.setFocus(dayNode.path("focus").asText());
        day.setExercises(buildExercises(dayNode.path("exercises")));

        return day;
    }

    // -------------------------------------------------------------------------
    // Exercises
    // -------------------------------------------------------------------------

    private List<ExerciseDto> buildExercises(JsonNode exercisesNode) {
        List<ExerciseDto> exercises = new ArrayList<>();

        if (exercisesNode.isMissingNode() || !exercisesNode.isArray()) {
            return exercises;
        }

        for (JsonNode exNode : exercisesNode) {
            exercises.add(buildExerciseDto(exNode));
        }

        return exercises;
    }

    private ExerciseDto buildExerciseDto(JsonNode exNode) {
        ExerciseDto exercise = new ExerciseDto();

        exercise.setName(exNode.path("name").asText());
        exercise.setSets(exNode.path("sets").asInt());
        exercise.setReps(exNode.path("reps").asText());       // String لأن ممكن يبعت "8-12" أو "failure"
        exercise.setRest_seconds(exNode.path("rest_seconds").asInt());
        exercise.setNotes(exNode.path("notes").asText());

        return exercise;
    }

    // -------------------------------------------------------------------------
    // Markdown fence stripping (safety net)
    // -------------------------------------------------------------------------

    private String stripMarkdownFences(String raw) {
        if (raw == null) return "";
        String stripped = raw.strip();
        if (stripped.startsWith("```")) {
            stripped = stripped.replaceFirst("^```(?:json)?\\s*", "");
            stripped = stripped.replaceFirst("\\s*```$", "");
        }
        return stripped.strip();
    }
}


