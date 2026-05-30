package com.abdelaziz26.metriplate.utils.ai;

import com.abdelaziz26.metriplate.entities.diet.Ingredient;
import com.abdelaziz26.metriplate.entities.user.Allergy;
import com.abdelaziz26.metriplate.entities.user.HealthMetrics;
import com.abdelaziz26.metriplate.enums.diet.DietGoal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WeeklyDietPlanPromptBuilder {

    @Value("classpath:prompts/weekly-diet-plan-with-meals.txt")
    private Resource promptTemplate;

    public String buildPrompt(
            HealthMetrics metrics,
            List<Ingredient> availableIngredients,
            List<Allergy> userAllergies,
            LocalDate startDate,
            int weekNumber
    ) throws IOException {

        // Read prompt template via Spring Resource

        log.info("Step 1 - Reading template");
        String template;
        try (InputStream is = promptTemplate.getInputStream()) {
            template = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }

        log.info("Step 2 - Template loaded");

        // Calculate derived values
        LocalDate endDate = startDate.plusDays(6);
        int dailyCalories = metrics.getTdee();

        // Format ingredient list
        String ingredientList = formatIngredients(availableIngredients);

        log.info("Step 3 - Ingredients formatted");

        // Format allergy list
        String allergyList = formatAllergies(userAllergies);

        log.info("Step 4 - Allergies formatted");

        // Replace all placeholders
        String prompt = template
                // User Profile
                .replace("{{userAge}}", safe(metrics.getAge()))
                .replace("{{userGender}}",
                        metrics.getGender() != null ? metrics.getGender().name() : "Not provided")
                .replace("{{userWeight}}", safe(metrics.getWeightKg()))
                .replace("{{userHeight}}", safe(metrics.getHeightCm()))

                // Body Composition
                .replace("{{muscleMass}}",
                        metrics.getMuscleMassKg() != null
                                ? String.valueOf(metrics.getMuscleMassKg())
                                : "Not provided")
                .replace("{{bodyFatPercentage}}",
                        metrics.getFatPercentage() != null
                                ? String.valueOf(metrics.getFatPercentage())
                                : "Not provided")
                .replace("{{fatMass}}",
                        metrics.getFatMass() != null
                                ? String.valueOf(metrics.getFatMass())
                                : "Not provided")
                .replace("{{leanMass}}",
                        metrics.getLeanMass() != null
                                ? String.valueOf(metrics.getLeanMass())
                                : "Not provided")
                .replace("{{bodyFatCategory}}",
                        safe(metrics.getBodyFatCategory()))

                // Metabolic
                .replace("{{bmi}}", safe(metrics.getBmi()))
                .replace("{{bmiCategory}}",
                        safe(metrics.getBmiCategory()))
                .replace("{{tdee}}", safe(metrics.getTdee()))

                // Goals & Preferences
                .replace("{{dietGoal}}",
                        metrics.getDietGoal() != null
                                ? metrics.getDietGoal().name()
                                : "Not provided")
                .replace("{{dietType}}",
                        metrics.getDietType() != null
                                ? metrics.getDietType().name()
                                : "Not provided")
                .replace("{{activityLevel}}",
                        metrics.getActivityLevel() != null
                                ? metrics.getActivityLevel().name()
                                : "Not provided")
                .replace("{{targetWeight}}",
                        metrics.getTargetWeightKg() != null
                                ? String.valueOf(metrics.getTargetWeightKg())
                                : "Not set")
                .replace("{{medicalNotes}}",
                        safe(metrics.getMedicalNotes()))

                // Nutrition Targets
                .replace("{{dailyCalories}}", String.valueOf(dailyCalories))
                .replace("{{dailyProtein}}", String.valueOf(calculateDailyProtein(metrics)))
                .replace("{{dailyCarbs}}", String.valueOf(calculateDailyCarbs(metrics)))
                .replace("{{dailyFat}}", String.valueOf(calculateDailyFat(metrics)))
                .replace("{{dailyFiber}}", "30")
                .replace("{{dailySugar}}", "50")
                .replace("{{waterGoal}}", "3000")

                // Restrictions
                .replace("{{allergyList}}", safe(allergyList))

                // Ingredients
                .replace("{{availableIngredientsList}}", safe(ingredientList))

                // Plan Duration
                .replace("{{startDate}}", safe(startDate))
                .replace("{{endDate}}", safe(endDate))
                .replace("{{weekNumber}}", String.valueOf(weekNumber));

        log.info("Step 5 - Prompt generated");

        return prompt;
    }

    private String safe(Object value) {
        return value == null ? "" : value.toString();
    }

    private String formatIngredients(List<Ingredient> ingredients) {
        return ingredients.stream()
                .map(ingredient -> String.format(
                        "- ID: %d\n" +
                                "- Name: %s\n" +
                                "- Category: %s\n" +
                                "- Calories (per 100g): %.1f\n" +
                                "- Protein (g/100g): %.1f\n" +
                                "- Carbs (g/100g): %.1f\n" +
                                "- Fat (g/100g): %.1f\n" +
                                "- Fiber (g/100g): %.1f\n" +
                                "- Sugar (g/100g): %.1f\n",
                        ingredient.getId(),
                        ingredient.getName(),
                        ingredient.getCategory().name(),
                        ingredient.getCalories(),
                        ingredient.getProteinG(),
                        ingredient.getCarbsG(),
                        ingredient.getFatG(),
                        ingredient.getFiberG(),
                        ingredient.getSugarG()
                ))
                .collect(Collectors.joining("\n"));
    }

    private String formatAllergies(List<Allergy> allergies) {
        if (allergies == null || allergies.isEmpty()) return "None";
        return allergies.stream()
                .map(Allergy::getName)
                .collect(Collectors.joining(", "));
    }

    private Double calculateDailyProtein(HealthMetrics metrics) {
        double multiplier = metrics.getDietGoal() == DietGoal.GAIN_MUSCLE ? 2.0 : 1.6;
        return metrics.getWeightKg() * multiplier;
    }

    private Double calculateDailyCarbs(HealthMetrics metrics) {
        double proteinCals = calculateDailyProtein(metrics) * 4;
        double fatCals = calculateDailyFat(metrics) * 9;
        return (metrics.getTdee() - proteinCals - fatCals) / 4;
    }

    private Double calculateDailyFat(HealthMetrics metrics) {
        return metrics.getWeightKg() * 1.0;
    }
}
