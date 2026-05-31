package com.abdelaziz26.metriplate.utils.ai;

import com.abdelaziz26.metriplate.entities.user.HealthMetrics;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class WorkoutPromptBuilder {

    @Value("classpath:prompts/workout-plan.txt")
    private Resource promptTemplate;

    public String buildPrompt(HealthMetrics metrics) throws IOException {

        // Read prompt template via Spring Resource
        String template;
        try (InputStream is = promptTemplate.getInputStream()) {
            template = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }

        return template

                // User Profile
                .replace("{{userAge}}", safe(metrics.getAge()))
                .replace("{{userGender}}", enumSafe(metrics.getGender()))
                .replace("{{userWeight}}", safe(metrics.getWeightKg()))
                .replace("{{userHeight}}", safe(metrics.getHeightCm()))

                // Body Composition
                .replace("{{muscleMass}}", safe(metrics.getMuscleMassKg()))
                .replace("{{bodyFatPercentage}}", safe(metrics.getFatPercentage()))
                .replace("{{fatMass}}", safe(metrics.getFatMass()))
                .replace("{{leanMass}}", safe(metrics.getLeanMass()))
                .replace("{{bodyFatCategory}}", safe(metrics.getBodyFatCategory()))

                // Metabolic
                .replace("{{bmi}}", safe(metrics.getBmi()))
                .replace("{{bmiCategory}}", safe(metrics.getBmiCategory()))
                .replace("{{tdee}}", safe(metrics.getTdee()))

                // Goals & Preferences
                .replace("{{dietGoal}}",
                        metrics.getDietGoal() != null
                                ? metrics.getDietGoal().name()
                                : "Not provided")
                .replace("{{activityLevel}}",
                        metrics.getActivityLevel() != null
                                ? metrics.getActivityLevel().name()
                                : "Not provided")
                .replace("{{targetWeight}}",
                        metrics.getTargetWeightKg() != null
                                ? metrics.getTargetWeightKg().toString()
                                : "Not set")
                .replace("{{medicalNotes}}",
                        metrics.getMedicalNotes() != null
                                ? metrics.getMedicalNotes()
                                : "None");

    }

    private String safe(Object value) {
        return value == null ? "" : value.toString();
    }
    private String enumSafe(Enum<?> value) {
        return value == null ? "Not provided" : value.name();
    }
}

