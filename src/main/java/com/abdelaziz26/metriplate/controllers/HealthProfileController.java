package com.abdelaziz26.metriplate.controllers;

import com.abdelaziz26.metriplate.dtos.metrics.CreateHealthMetricDto;
import com.abdelaziz26.metriplate.dtos.metrics.ReadHealthMetricDto;
import com.abdelaziz26.metriplate.dtos.metrics.UpdateHealthMetricDto;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.services.healthMetrics.MetricsService;
import com.abdelaziz26.metriplate.utils._Abdel3zizController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health-profile")
@RequiredArgsConstructor
public class HealthProfileController extends _Abdel3zizController {
    private final MetricsService metricsService;

    @GetMapping("/me")
    public ResponseEntity<@NotNull Result<ReadHealthMetricDto, Error>> getMyHealthProfile() {
        Result<ReadHealthMetricDto, Error> result = metricsService.getHealthMetricsByUserId();
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NotNull Result<ReadHealthMetricDto, Error>> getHealthProfileById(@PathVariable Long id) {
        Result<ReadHealthMetricDto, Error> result = metricsService.getHealthMetricsById(id);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @PostMapping
    public ResponseEntity<@NotNull Result<ReadHealthMetricDto, Error>> addHealthProfile(@Valid @RequestBody CreateHealthMetricDto createDto) {
        Result<ReadHealthMetricDto, Error> result = metricsService.addHealthMetrics(createDto);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<@NotNull Result<ReadHealthMetricDto, Error>> updateHealthProfile(@PathVariable Long id, @Valid @RequestBody UpdateHealthMetricDto updateDto) {
        Result<ReadHealthMetricDto, Error> result = metricsService.updateHealthMetrics(id, updateDto);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<@NotNull Result<String, Error>> deleteHealthProfile(@PathVariable Long id) {
        Result<String, Error> result = metricsService.deleteHealthMetrics(id);
        return new ResponseEntity<>(result, resolveStatus(result));
    }
}
