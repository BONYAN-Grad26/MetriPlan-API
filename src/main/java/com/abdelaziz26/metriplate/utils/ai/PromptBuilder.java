//package com.abdelaziz26.metriplate.utils.ai;
//
//import com.abdelaziz26.metriplate.entities.user.Allergy;
//import com.abdelaziz26.metriplate.entities.user.Goal;
//import com.abdelaziz26.metriplate.entities.user.HealthMetrics;
//import com.abdelaziz26.metriplate.entities.diet.Ingredient;
//import com.abdelaziz26.metriplate.enums.ProgressStatus;
//import com.abdelaziz26.metriplate.enums.AllergenType;
//import com.abdelaziz26.metriplate.exceptions.PlanGenerationException;
//import com.abdelaziz26.metriplate.repositories.AllergyRepository;
//import com.abdelaziz26.metriplate.repositories.GoalRepository;
//import com.abdelaziz26.metriplate.repositories.IngredientRepository;
//import com.abdelaziz26.metriplate.repositories.MetricsRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class PromptBuilder {
//
//
//    private final GoalRepository goalRepo;
//    private final MetricsRepository healthRepo;
//    private final AllergyRepository allergyRepo;
//    private final IngredientRepository ingredientRepo;
//
//    private final String template;
//
//    public PromptBuilder(GoalRepository goalRepo,
//                               MetricsRepository healthRepo,
//                               AllergyRepository allergyRepo,
//                               IngredientRepository ingredientRepo) {
//        this.goalRepo       = goalRepo;
//        this.healthRepo     = healthRepo;
//        this.allergyRepo    = allergyRepo;
//        this.ingredientRepo = ingredientRepo;
//        this.template       = loadTemplate();
//    }
//
//
//    public String build(Long userId) throws PlanGenerationException {
//
//        // 1. Active goal  (must exist)
//        Goal goal = goalRepo
//                .findByUserIdAndStatus(userId, ProgressStatus.IN_PROGRESS)
//                .orElseThrow(() -> new PlanGenerationException(
//                        "No active goal found for user " + userId));
//
//        // 2. Latest health metrics  (must exist)
//        HealthMetrics hm = healthRepo
//                .findTopByUserIdOrderByRecordedAtDesc(userId)
//                .orElseThrow(() -> new PlanGenerationException(
//                        "No health-metrics snapshot found for user " + userId));
//
//        // 3. Allergies  (may be empty – that's fine)
//        List<Allergy> allergies = allergyRepo.findByUserId(userId);
//
//        // 4. All ingredients with their dietary tags
//        List<Ingredient> ingredients = ingredientRepo.findAllWithDietaryTags();
//
//        // 5. Filter out ingredients that contain allergens the user is allergic to
//        List<AllergenType> allergenicTypes = allergies.stream()
//                .map(Allergy::getType)
//                .toList();
//
//        List<Ingredient> safeIngredients = ingredients.stream()
//                .filter(ing -> {
//                    // an ingredient is unsafe if ANY of its allergens matches user's allergy types
//                    return ing.getAllergens().stream()
//                            .noneMatch(allergen -> allergenicTypes.contains(allergen.getType()));
//                })
//                .toList();
//
//        if (safeIngredients.isEmpty()) {
//            throw new PlanGenerationException(
//                    "Zero safe ingredients remain after allergy filtering for user " + userId);
//        }
//
//        // 6. Date math  (today = week-1 start)
//        LocalDate today       = LocalDate.now();
//        LocalDate endWeek1    = today.plusDays(6);
//        LocalDate startWeek2  = today.plusDays(7);
//        LocalDate endWeek2    = today.plusDays(13);
//
//        // 7. Replace placeholders
//        return template
//                // ── goal ──
//                .replace("{goalType}",        goal.getGoalType().name().replace("_", " "))
//                .replace("{goalDescription}", safe(goal.getDescription()))
//                .replace("{targetWeight}",    nullableDouble(goal.getTargetWeight()))
//                .replace("{targetBodyFat}",   nullableDouble(goal.getTargetBodyFat()))
//                .replace("{targetCalories}",  nullableDouble(goal.getTargetCalories()))
//                // ── health metrics ──
//                .replace("{bmi}",                  nullableDouble(hm.getBmi()))
//                .replace("{bodyFatPercentage}",    nullableDouble(hm.getBodyFatPercentage()))
//                .replace("{muscleMass}",           nullableDouble(hm.getMuscleMass()))
//                .replace("{waistCircumference}",   nullableDouble(hm.getWaistCircumference()))
//                .replace("{hipCircumference}",     nullableDouble(hm.getHipCircumference()))
//                // ── allergies ──
//                .replace("{allergyList}",    allergies.isEmpty()
//                        ? "None"
//                        : allergies.stream()
//                        .map(a -> a.getName() + " (" + a.getType().name() + ")")
//                        .collect(Collectors.joining(", ")))
//                // ── ingredients ──
//                .replace("{ingredientList}", safeIngredients.stream()
//                        .map(Ingredient::getName)
//                        .collect(Collectors.joining(", ")))
//                // ── dates ──
//                .replace("{startDate}",      fmt(today))
//                .replace("{endDateWeek1}",   fmt(endWeek1))
//                .replace("{startDateWeek2}", fmt(startWeek2))
//                .replace("{endDate}",        fmt(endWeek2));
//    }
//
//    // ═══════════════════════════════════════════════════
//    //  PRIVATE
//    // ═══════════════════════════════════════════════════
//
//    private static String fmt(LocalDate d)           { return d.format(DateTimeFormatter.ISO_LOCAL_DATE); }
//    private static String safe(String s)             { return s == null ? "" : s; }
//    private static String nullableDouble(Double d)   { return d == null ? "N/A" : String.valueOf(d); }
//
//    private static String loadTemplate() {
//        try {
//            return new String(
//                    new ClassPathResource("prompts/diet_plan.txt")
//                            .getInputStream().readAllBytes(),
//                    StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            throw new IllegalStateException("Cannot load diet_plan.txt from classpath", e);
//        }
//    }
//}
