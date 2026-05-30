package com.abdelaziz26.metriplate.controllers;

import com.abdelaziz26.metriplate.dtos.plan.DayDTO;
import com.abdelaziz26.metriplate.dtos.plan.WeekDTO;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.services.dietPlan.DietPlanService;
import com.abdelaziz26.metriplate.utils._Abdel3zizController;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/diet-plan")
@RequiredArgsConstructor
public class DietPlanController extends _Abdel3zizController {

	private final DietPlanService dietPlanService;

	@PostMapping("/generate-weekly")
	public ResponseEntity<@NotNull Result<WeekDTO, Error>> generateWeeklyPlan(
			@RequestParam LocalDate startDate,
			@RequestParam(defaultValue = "1") int weekNumber
	) throws Exception {
        Result<WeekDTO, Error> res = dietPlanService.generateWeeklyPlan(startDate, weekNumber);
		return ResponseEntity.status(resolveStatus(res)).body(res);
	}

	@GetMapping("/weekly")
	public ResponseEntity<@NotNull Result<List<WeekDTO>, Error>> getWeeklyPlansByUserId() {
		Result<List<WeekDTO>, Error> res = dietPlanService.getWeeklyPlansByUserId();
		return ResponseEntity.status(resolveStatus(res)).body(res);
	}

	@GetMapping("/{planId}")
	public ResponseEntity<@NotNull Result<WeekDTO, Error>> getWeeklyPlanById(@PathVariable Long planId) {
		Result<WeekDTO, Error> res = dietPlanService.getWeeklyPlanById(planId);
		return ResponseEntity.status(resolveStatus(res)).body(res);
	}

	@GetMapping("/daily/{planId}")
	public ResponseEntity<@NotNull Result<WeekDTO, Error>> getDailyPlanById(@PathVariable Long planId) {
		Result<WeekDTO, Error> res = dietPlanService.getDailyPlanById(planId);
		return ResponseEntity.status(resolveStatus(res)).body(res);
	}

	@GetMapping("/today")
	public ResponseEntity<@NotNull Result<DayDTO, Error>> getTodayPlan() {
		Result<DayDTO, Error> res = dietPlanService.getTodayPlan();
		return ResponseEntity.status(resolveStatus(res)).body(res);
	}

	@PutMapping("/{planId}")
	public ResponseEntity<@NotNull Result<WeekDTO, Error>> updateWeeklyPlan(@PathVariable Long planId,
	                                                                         @RequestBody WeekDTO updatedPlan) {
		Result<WeekDTO, Error> res = dietPlanService.updateWeeklyPlan(planId, updatedPlan);
		return ResponseEntity.status(resolveStatus(res)).body(res);
	}

	@DeleteMapping("/{planId}")
	public ResponseEntity<@NotNull Result<String, Error>> deleteWeeklyPlan(@PathVariable Long planId) {
		Result<String, Error> res = dietPlanService.deleteWeeklyPlan(planId);
		return ResponseEntity.status(resolveStatus(res)).body(res);
	}

}
