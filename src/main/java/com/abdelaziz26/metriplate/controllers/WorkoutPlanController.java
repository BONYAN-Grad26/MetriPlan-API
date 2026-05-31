package com.abdelaziz26.metriplate.controllers;

import com.abdelaziz26.metriplate.dtos.workout.WorkoutDayDto;
import com.abdelaziz26.metriplate.dtos.workout.WorkoutPlanResponseDto;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.services.workout.WorkoutService;
import com.abdelaziz26.metriplate.services.workout.WorkoutServiceImpl;
import com.abdelaziz26.metriplate.utils._Abdel3zizController;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workout-plan")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkoutPlanController extends _Abdel3zizController {
    WorkoutService workoutService;

    @PostMapping("/generate-weekly")
    public ResponseEntity<@NotNull Result<WorkoutPlanResponseDto, Error>> generateWeeklyWorkoutPlan() throws Exception {
        Result<WorkoutPlanResponseDto, Error> res = workoutService.generateWeeklyWorkoutPlan();
        return ResponseEntity.status(resolveStatus(res)).body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NotNull Result<WorkoutPlanResponseDto, Error>> getById(
            @PathVariable Long id) {

        Result<WorkoutPlanResponseDto, Error> res = workoutService.getById(id);
        return ResponseEntity.status(resolveStatus(res)).body(res);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<@NotNull Result<List<WorkoutPlanResponseDto>, Error>> getByUserId(
            @PathVariable Long userId) {

        Result<List<WorkoutPlanResponseDto>, Error> res = workoutService.getByUserId(userId);
        return ResponseEntity.status(resolveStatus(res)).body(res);
    }

    @GetMapping("/today")
    public ResponseEntity<@NotNull Result<WorkoutDayDto, Error>> getTodayPlan() {

        Result<WorkoutDayDto, Error> res = workoutService.getTodayPlan();
        return ResponseEntity.status(resolveStatus(res)).body(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<@NotNull Result<String, Error>> deletePlan(
            @PathVariable Long id) {

        Result<String, Error> res = workoutService.deletePlan(id);
        return ResponseEntity.status(resolveStatus(res)).body(res);
    }
}
