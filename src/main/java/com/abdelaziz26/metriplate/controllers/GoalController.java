package com.abdelaziz26.metriplate.controllers;

import com.abdelaziz26.metriplate.dtos.goal.CreateGoalDto;
import com.abdelaziz26.metriplate.dtos.goal.GoalSummaryDto;
import com.abdelaziz26.metriplate.dtos.goal.ReadGoalDto;
import com.abdelaziz26.metriplate.dtos.goal.UpdateGoalDto;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.services.goal.GoalService;
import com.abdelaziz26.metriplate.utils._Abdel3zizController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goal")
@RequiredArgsConstructor
public class GoalController extends _Abdel3zizController {
    private final GoalService goalService;

    @GetMapping("/me")
    public ResponseEntity<@NotNull Result<List<ReadGoalDto>, Error>> getMyGoals() {
        Result<List<ReadGoalDto>, Error> result = goalService.getGoalsByUserId();
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<@NotNull Result<List<GoalSummaryDto>, Error>> getGoalsByUserId(@PathVariable Long userId) {
        Result<List<GoalSummaryDto>, Error> result = goalService.getGoalsByUserId(userId);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @PostMapping
    public ResponseEntity<@NotNull Result<ReadGoalDto, Error>> addGoal(@Valid @RequestBody CreateGoalDto createGoalDto) {
        Result<ReadGoalDto, Error> result = goalService.AddGoal(createGoalDto);
        return new ResponseEntity<>(result, resolveStatus(result));
    }


    @PutMapping("/{id}")
    public ResponseEntity<@NotNull Result<ReadGoalDto, Error>> updateGoal(@PathVariable Long id, @Valid @RequestBody UpdateGoalDto updateGoalDto) {
        Result<ReadGoalDto, Error> result = goalService.UpdateGoal(id, updateGoalDto);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<@NotNull Result<String, Error>> deleteGoal(@PathVariable Long id) {
        Result<String, Error> result = goalService.DeleteGoal(id);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

}
