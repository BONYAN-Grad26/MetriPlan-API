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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WorkoutJsonParser {

    private final ObjectMapper objectMapper;

    public WorkoutPlanResponseDto parseWorkoutPlanResponse(String jsonResponse)
            throws IOException, PlanGenerationException {

        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        if (rootNode.has("error")) {
            throw new PlanGenerationException("LLM returned error: " + rootNode.get("reason").asText());
        }

        WorkoutPlanResponseDto planDto = new WorkoutPlanResponseDto();

        if (rootNode.has("plan_name")) {
            planDto.setPlan_name(rootNode.get("plan_name").asText());
        }

        if (rootNode.has("split_type")) {
            planDto.setSplit_type(rootNode.get("split_type").asText());
        }

        if (rootNode.has("split_reasoning")) {
            planDto.setSplit_reasoning(rootNode.get("split_reasoning").asText());
        }

        // Parse weekly schedule
        Map<String, WorkoutDayDto> weeklySchedule = new HashMap<>();
        JsonNode scheduleNode = rootNode.get("weekly_schedule");
        if (scheduleNode != null && scheduleNode.isObject()) {
            var iterator = scheduleNode.fields();
            while (iterator.hasNext()) {
                var entry = iterator.next();
                try {
                    String day = entry.getKey();
                    JsonNode dayNode = entry.getValue();
                    WorkoutDayDto dayDto = parseWorkoutDay(dayNode);
                    weeklySchedule.put(day, dayDto);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        planDto.setWeekly_schedule(weeklySchedule);

        return planDto;
    }

    public WorkoutDayDto parseWorkoutDay(JsonNode dayNode) throws IOException {
        WorkoutDayDto dayDto = new WorkoutDayDto();

        if (dayNode.has("session")) {
            dayDto.setSession(dayNode.get("session").asText());
        }

        if (dayNode.has("focus")) {
            dayDto.setFocus(dayNode.get("focus").asText());
        }

        // Parse exercises
        List<ExerciseDto> exercises = new ArrayList<>();
        JsonNode exercisesNode = dayNode.get("exercises");
        if (exercisesNode != null && exercisesNode.isArray()) {
            for (JsonNode exerciseNode : exercisesNode) {
                ExerciseDto exerciseDto = parseExercise(exerciseNode);
                exercises.add(exerciseDto);
            }
        }
        dayDto.setExercises(exercises);

        return dayDto;
    }

    public ExerciseDto parseExercise(JsonNode exerciseNode) {
        ExerciseDto exerciseDto = new ExerciseDto();

        if (exerciseNode.has("name")) {
            exerciseDto.setName(exerciseNode.get("name").asText());
        }

        if (exerciseNode.has("sets")) {
            exerciseDto.setSets(exerciseNode.get("sets").asInt());
        }

        if (exerciseNode.has("reps")) {
            exerciseDto.setReps(exerciseNode.get("reps").asText());
        }

        if (exerciseNode.has("rest_seconds")) {
            exerciseDto.setRest_seconds(exerciseNode.get("rest_seconds").asInt());
        }

        if (exerciseNode.has("notes")) {
            exerciseDto.setNotes(exerciseNode.get("notes").asText());
        }

        return exerciseDto;
    }
}


