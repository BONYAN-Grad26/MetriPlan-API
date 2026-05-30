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

        // Replace all placeholders
        String prompt = template
                // User Profile
                .replace("{{userAge}}", String.valueOf(metrics.getAge()))
                .replace("{{userGender}}", metrics.getGender().name())
                .replace("{{userWeight}}", String.valueOf(metrics.getWeightKg()))
                .replace("{{userHeight}}", String.valueOf(metrics.getHeightCm()))

                // Body Composition
                .replace("{{muscleMass}}",
                        metrics.getMuscleMassKg() != null ? String.valueOf(metrics.getMuscleMassKg()) : "Not provided")
                .replace("{{bodyFatPercentage}}",
                        metrics.getFatPercentage() != null ? String.valueOf(metrics.getFatPercentage()) : "Not provided")
                .replace("{{fatMass}}",
                        metrics.getFatMass() != null ? String.valueOf(metrics.getFatMass()) : "Not provided")
                .replace("{{leanMass}}",
                        metrics.getLeanMass() != null ? String.valueOf(metrics.getLeanMass()) : "Not provided")
                .replace("{{bodyFatCategory}}",
                        metrics.getBodyFatCategory() != null ? metrics.getBodyFatCategory() : "Not provided")

                // Metabolic
                .replace("{{bmi}}", String.valueOf(metrics.getBmi()))
                .replace("{{bmiCategory}}", metrics.getBmiCategory())
                .replace("{{tdee}}", String.valueOf(metrics.getTdee()))

                // Goals & Preferences
                .replace("{{dietGoal}}", metrics.getDietGoal().name())
                .replace("{{activityLevel}}", metrics.getActivityLevel().name())
                .replace("{{targetWeight}}",
                        metrics.getTargetWeightKg() != null ? String.valueOf(metrics.getTargetWeightKg()) : "Not set")
                .replace("{{medicalNotes}}",
                        metrics.getMedicalNotes() != null ? metrics.getMedicalNotes() : "None");

        return prompt;
    }
}

