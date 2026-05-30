package com.abdelaziz26.metriplate.utils.ai;

import com.abdelaziz26.metriplate.entities.diet.Ingredient;
import com.abdelaziz26.metriplate.entities.user.Allergy;
import com.abdelaziz26.metriplate.entities.user.HealthMetrics;
import com.abdelaziz26.metriplate.enums.diet.DietGoal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
        String template;
        try (InputStream is = promptTemplate.getInputStream()) {
            template = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }

        // Calculate derived values
        LocalDate endDate = startDate.plusDays(6);
        int dailyCalories = metrics.getTdee();

        // Format ingredient list
        String ingredientList = formatIngredients(availableIngredients);

        // Format allergy list
        String allergyList = formatAllergies(userAllergies);

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
                .replace("{{dietType}}", metrics.getDietType().name())
                .replace("{{activityLevel}}", metrics.getActivityLevel().name())
                .replace("{{targetWeight}}",
                        metrics.getTargetWeightKg() != null ? String.valueOf(metrics.getTargetWeightKg()) : "Not set")
                .replace("{{medicalNotes}}",
                        metrics.getMedicalNotes() != null ? metrics.getMedicalNotes() : "None")

                // Nutrition Targets
                .replace("{{dailyCalories}}", String.valueOf(dailyCalories))
                .replace("{{dailyProtein}}", String.valueOf(calculateDailyProtein(metrics)))
                .replace("{{dailyCarbs}}", String.valueOf(calculateDailyCarbs(metrics)))
                .replace("{{dailyFat}}", String.valueOf(calculateDailyFat(metrics)))
                .replace("{{dailyFiber}}", "30")
                .replace("{{dailySugar}}", "50")
                .replace("{{waterGoal}}", "3000")

                // Restrictions
                .replace("{{allergyList}}", allergyList)

                // Ingredients
                .replace("{{availableIngredientsList}}", ingredientList)

                // Plan Duration
                .replace("{{startDate}}", startDate.toString())
                .replace("{{endDate}}", endDate.toString())
                .replace("{{weekNumber}}", String.valueOf(weekNumber));

        return prompt;
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
