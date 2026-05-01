//package com.abdelaziz26.metriplate.controllers;
//
//import com.abdelaziz26.metriplate.dtos.plan.DietPlanDTO;
//import com.abdelaziz26.metriplate.dtos.plan.DietPlanSimpleResponseDto;
//import com.abdelaziz26.metriplate.exceptions.PlanGenerationException;
//import com.abdelaziz26.metriplate.responses.Result_.Error;
//import com.abdelaziz26.metriplate.responses.Result_.Result;
//import com.abdelaziz26.metriplate.services.ai.AiService;
//import com.abdelaziz26.metriplate.services.plan.PlanService;
//import com.abdelaziz26.metriplate.utils._Abdel3zizController;
//import lombok.RequiredArgsConstructor;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/plan")
//@RequiredArgsConstructor
//public class PlanController extends _Abdel3zizController {
//
//    private final AiService geminiService;
//    private final PlanService planService;
//
//    @PostMapping("/generate")
//    public ResponseEntity<@NotNull Result<DietPlanDTO, Error>> generateDietPlan() throws PlanGenerationException {
//        Result<DietPlanDTO, Error> result = geminiService.generateAndSaveDietPlan();
//        return ResponseEntity.status(resolveStatus(result)).body(result);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<@NotNull Result<DietPlanDTO, Error>> getById(@PathVariable Long id) {
//        Result<DietPlanDTO, Error> result = planService.getById(id);
//        return ResponseEntity.status(resolveStatus(result)).body(result);
//    }
//
//    @GetMapping("/all")
//    public ResponseEntity<@NotNull Result<List<DietPlanSimpleResponseDto>, Error>> getMyPlans(){
//        Result<List<DietPlanSimpleResponseDto>, Error> result = planService.getByUserId();
//        return ResponseEntity.status(resolveStatus(result)).body(result);
//    }
//}
