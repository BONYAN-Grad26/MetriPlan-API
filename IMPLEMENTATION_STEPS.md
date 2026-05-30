# LLM Weekly Diet Plan Generation - Implementation Steps

## Overview
This document provides step-by-step instructions to implement the LLM-based weekly diet plan generation in your MetriPlate application.

## Files Created
1. **weekly-diet-plan-with-meals.txt** - The actual prompt template to send to LLM
2. **WEEKLY_DIET_PLAN_PROMPT_GUIDE.md** - Comprehensive guide with code examples
3. **WEEKLY_DIET_PLAN_EXAMPLE_RESPONSE.json** - Example JSON response from LLM
4. **IMPLEMENTATION_STEPS.md** - This file

---

## Step 1: Create Prompt Builder Service

Create a service to build the prompt with actual data:

```java
package com.abdelaziz26.metriplate.services.llm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
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
        
        // Read prompt template
        String template = Files.readString(Paths.get(promptTemplate.getURI()));
        
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
        if (allergies.isEmpty()) return "None";
        return allergies.stream()
            .map(Allergy::getAllergenName)
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
```

---

## Step 2: Create LLM Client Service

Create a service to communicate with your LLM provider (Gemini, GPT-4, Claude, etc.):

```java
package com.abdelaziz26.metriplate.services.llm;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LLMClient {
    
    @Value("${gemini.project-id}")
    private String projectId;
    
    @Value("${gemini.location}")
    private String location;
    
    @Value("${gemini.model-name:gemini-1.5-pro}")
    private String modelName;
    
    public String generateContent(String prompt) {
        try (VertexAI vertexAI = new VertexAI(projectId, location)) {
            GenerativeModel model = new GenerativeModel(modelName, vertexAI);
            
            String textContent = model.generateContent(prompt)
                .getContent()
                .getParts(0)
                .getText();
            
            return textContent;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate content from LLM", e);
        }
    }
}
```

---

## Step 3: Create Plan Parsing & Persistence Service

Create a service to parse the JSON response and save to database:

```java
package com.abdelaziz26.metriplate.services.dietPlan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WeeklyPlanGenerationService {

    private final ObjectMapper objectMapper;
    private final LLMClient llmClient;
    private final WeeklyDietPlanPromptBuilder promptBuilder;
    private final WeeklyPlanRepository weeklyPlanRepository;
    private final DailyPlanRepository dailyPlanRepository;
    private final MealRepository mealRepository;
    private final MealIngredientRepository mealIngredientRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public WeeklyPlanGenerationService(
            ObjectMapper objectMapper,
            LLMClient llmClient,
            WeeklyDietPlanPromptBuilder promptBuilder,
            WeeklyPlanRepository weeklyPlanRepository,
            DailyPlanRepository dailyPlanRepository,
            MealRepository mealRepository,
            MealIngredientRepository mealIngredientRepository,
            IngredientRepository ingredientRepository
    ) {
        this.objectMapper = objectMapper;
        this.llmClient = llmClient;
        this.promptBuilder = promptBuilder;
        this.weeklyPlanRepository = weeklyPlanRepository;
        this.dailyPlanRepository = dailyPlanRepository;
        this.mealRepository = mealRepository;
        this.mealIngredientRepository = mealIngredientRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Transactional
    public WeeklyPlan generateWeeklyPlan(
            Long userId,
            LocalDate startDate,
            int weekNumber
    ) throws Exception {

        // 1. Fetch user's health metrics and available ingredients
        HealthMetrics metrics = healthMetricsRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Health metrics not found for user: " + userId));

        List<Ingredient> availableIngredients = ingredientRepository.findAllAvailable();
        List<Allergy> userAllergies = allergyRepository.findByUserId(userId);

        // 2. Build prompt
        String prompt = promptBuilder.buildPrompt(
                metrics,
                availableIngredients,
                userAllergies,
                startDate,
                weekNumber
        );

        // 3. Call LLM
        String llmResponse = llmClient.generateContent(prompt);

        // 4. Parse JSON response
        JsonNode responseNode = objectMapper.readTree(llmResponse);

        // 5. Check for errors
        if (responseNode.has("error")) {
            throw new PlanGenerationException(
                    "LLM failed to generate plan: " + responseNode.get("reason").asText()
            );
        }

        // 6. Create WeeklyPlan entity and save
        WeeklyPlan weeklyPlan = parseWeeklyPlanFromJson(responseNode, userId);
        WeeklyPlan savedWeeklyPlan = weeklyPlanRepository.save(weeklyPlan);

        // 7. Parse and save daily plans
        parseDailyPlans(responseNode, savedWeeklyPlan);

        return savedWeeklyPlan;
    }

    private WeeklyPlan parseWeeklyPlanFromJson(JsonNode responseNode, Long userId) {
        WeeklyPlan weeklyPlan = new WeeklyPlan();

        weeklyPlan.setWeekNumber(responseNode.get("weekNumber").asInt());
        weeklyPlan.setStartDate(LocalDate.parse(responseNode.get("startDate").asText()));
        weeklyPlan.setEndDate(LocalDate.parse(responseNode.get("endDate").asText()));
        weeklyPlan.setWeeklyCalorieTarget(responseNode.get("weeklyCalorieTarget").asDouble());
        weeklyPlan.setWeeklyProteinTarget(responseNode.get("weeklyProteinTarget").asDouble());
        weeklyPlan.setWeeklyCarbTarget(responseNode.get("weeklyCarbTarget").asDouble());
        weeklyPlan.setWeeklyFatTarget(responseNode.get("weeklyFatTarget").asDouble());
        weeklyPlan.setWeeklyStrategy(responseNode.get("weeklyStrategy").asText());
        weeklyPlan.setAiPreparationTips(responseNode.get("aiPreparationTips").asText());

        return weeklyPlan;
    }

    private void parseDailyPlans(JsonNode responseNode, WeeklyPlan weeklyPlan) {
        List<DailyPlan> dailyPlans = new ArrayList<>();

        for (JsonNode dayNode : responseNode.get("days")) {
            DailyPlan dailyPlan = parseDailyPlan(dayNode, weeklyPlan);
            dailyPlans.add(dailyPlan);
        }

        dailyPlanRepository.saveAll(dailyPlans);
    }

    private DailyPlan parseDailyPlan(JsonNode dayNode, WeeklyPlan weeklyPlan) {
        DailyPlan dailyPlan = new DailyPlan();

        dailyPlan.setWeeklyPlan(weeklyPlan);
        dailyPlan.setDayOfWeek(dayNode.get("dayOfWeek").asInt());
        dailyPlan.setDate(LocalDate.parse(dayNode.get("date").asText()));
        dailyPlan.setTargetCalories(dayNode.get("targetCalories").asDouble());
        dailyPlan.setTargetProtein(dayNode.get("targetProtein").asDouble());
        dailyPlan.setTargetCarbs(dayNode.get("targetCarbs").asDouble());
        dailyPlan.setTargetFat(dayNode.get("targetFat").asDouble());
        dailyPlan.setTargetFiber(dayNode.get("targetFiber").asDouble());
        dailyPlan.setTargetSugar(dayNode.get("targetSugar").asDouble());
        dailyPlan.setWaterGoal(dayNode.get("waterGoal").asDouble());
        dailyPlan.setAiDailyTips(dayNode.get("aiDailyTips").asText());
        dailyPlan.setStatus(DayStatus.PLANNED);

        // Parse meals
        List<DailyPlanMeal> dailyPlanMeals = new ArrayList<>();
        int mealOrder = 1;

        for (JsonNode mealNode : dayNode.get("meals")) {
            DailyPlanMeal dailyPlanMeal = parseMeal(mealNode, dailyPlan, mealOrder++);
            dailyPlanMeals.add(dailyPlanMeal);
        }

        dailyPlan.setMeals(dailyPlanMeals);
        return dailyPlan;
    }

    private DailyPlanMeal parseMeal(JsonNode mealNode, DailyPlan dailyPlan, int mealOrder) {
        // Find or create Meal
        Meal meal = findOrCreateMeal(mealNode);

        // Parse and create MealIngredients
        for (JsonNode ingredientNode : mealNode.get("ingredients")) {
            Long ingredientId = ingredientNode.get("ingredientId").asLong();

            Ingredient ingredient = ingredientRepository.findById(ingredientId)
                    .orElseThrow(() -> new RuntimeException("Ingredient not found: " + ingredientId));

            MealIngredient mealIngredient = new MealIngredient();
            mealIngredient.setMeal(meal);
            mealIngredient.setIngredient(ingredient);
            mealIngredient.setQuantity(ingredientNode.get("quantity").asDouble());
            mealIngredient.setMeasurementUnit(ingredientNode.get("measurementUnit").asText());
            mealIngredient.setCalories(ingredientNode.get("calories").asDouble());
            mealIngredient.setProtein(ingredientNode.get("protein").asDouble());
            mealIngredient.setCarbs(ingredientNode.get("carbs").asDouble());
            mealIngredient.setFat(ingredientNode.get("fat").asDouble());

            mealIngredientRepository.save(mealIngredient);
        }

        // Create DailyPlanMeal
        DailyPlanMeal dailyPlanMeal = new DailyPlanMeal();
        dailyPlanMeal.setDailyPlan(dailyPlan);
        dailyPlanMeal.setMeal(meal);
        dailyPlanMeal.setMealOrder(mealOrder);
        dailyPlanMeal.setIsCustomized(false);

        return dailyPlanMeal;
    }

    private Meal findOrCreateMeal(JsonNode mealNode) {
        String mealName = mealNode.get("name").asText();
        MealType mealType = MealType.valueOf(mealNode.get("mealType").asText());

        return mealRepository.findByNameAndMealType(mealName, mealType)
                .orElseGet(() -> {
                    Meal newMeal = new Meal();
                    newMeal.setName(mealName);
                    newMeal.setDescription(mealNode.get("description").asText());
                    newMeal.setMealType(mealType);
                    newMeal.setPreparationInstructions(mealNode.get("preparationInstructions").asText());
                    newMeal.setPreparationTime(mealNode.get("preparationTime").asInt());

                    return mealRepository.save(newMeal);
                });
    }
}
```

---

## Step 4: Create Controller Endpoint

Create a controller endpoint to trigger plan generation:

```java
package com.abdelaziz26.metriplate.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diet-plans")
public class DietPlanController {
    
    private final WeeklyPlanGenerationService weeklyPlanGenerationService;
    private final UserRepository userRepository;
    
    @PostMapping("/generate-weekly-plan")
    public ResponseEntity<?> generateWeeklyPlan(
        @RequestParam LocalDate startDate,
        @RequestParam(defaultValue = "1") int weekNumber,
        Authentication authentication
    ) {
        try {
            // Get authenticated user
            User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Generate plan
            WeeklyPlan weeklyPlan = weeklyPlanGenerationService.generateWeeklyPlan(
                user.getId(),
                startDate,
                weekNumber
            );
            
            return ResponseEntity.ok(weeklyPlan);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("Failed to generate plan: " + e.getMessage()));
        }
    }
}
```

---

## Step 5: Configuration (application.yml)

Add these properties to your application configuration:

```yaml
gemini:
  project-id: your-google-cloud-project-id
  location: us-central1
  model-name: gemini-1.5-pro

app:
  llm:
    weekly-diet-plan-prompt-path: "classpath:prompts/weekly-diet-plan-with-meals.txt"
```

---

## Step 6: Add Dependencies (pom.xml)

Add Google Vertex AI dependency:

```xml
<dependency>
    <groupId>com.google.cloud</groupId>
    <artifactId>google-cloud-vertexai</artifactId>
    <version>1.6.0</version>
</dependency>
```

Or if using OpenAI:

```xml
<dependency>
    <groupId>com.theokanning.openai-gpt3-java</groupId>
    <artifactId>service</artifactId>
    <version>0.18.2</version>
</dependency>
```

---

## Step 7: Database Entities Recap

Ensure these entities exist and are properly set up:

### WeeklyPlan
- ✅ Already provided in attachment
- Contains: weekNumber, startDate, endDate, weekly targets, strategy, tips

### DailyPlan
- ✅ Contains: date, dayOfWeek, daily targets, meals, tips

### Meal
- ✅ Contains: name, mealType, description, preparationInstructions, preparationTime, ingredients

### MealIngredient
- ✅ Contains: meal, ingredient, quantity, measurementUnit, nutrition info

### Ingredient
- ✅ Contains: id, name, category, nutritional info (per 100g)

---

## Step 8: Testing

### Test Prompt Building
```java
@Test
public void testPromptBuilder() throws IOException {
    WeeklyDietPlanPromptBuilder builder = new WeeklyDietPlanPromptBuilder();
    HealthMetrics metrics = createTestMetrics();
    List<Ingredient> ingredients = createTestIngredients();
    List<Allergy> allergies = new ArrayList<>();
    
    String prompt = builder.buildPrompt(
        metrics,
        ingredients,
        allergies,
        LocalDate.now(),
        1
    );
    
    assertNotNull(prompt);
    assertTrue(prompt.contains("BREAKFAST"));
    assertTrue(prompt.contains("LUNCH"));
}
```

### Test LLM Response Parsing
```java
@Test
public void testResponseParsing() throws Exception {
    String jsonResponse = readExampleResponse(); // From WEEKLY_DIET_PLAN_EXAMPLE_RESPONSE.json
    
    WeeklyPlanGenerationService service = new WeeklyPlanGenerationService(...);
    WeeklyPlan plan = service.parseWeeklyPlanFromJson(
        objectMapper.readTree(jsonResponse),
        1L
    );
    
    assertEquals(1, plan.getWeekNumber());
    assertEquals(7, plan.getDailyPlans().size());
    assertEquals(4, plan.getDailyPlans().get(0).getMeals().size());
}
```

---

## API Usage Example

### Generate Weekly Plan
```bash
curl -X POST http://localhost:8080/api/diet-plans/generate-weekly-plan \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "startDate": "2026-05-19",
    "weekNumber": 1
  }'
```

### Response
```json
{
  "id": 1,
  "weekNumber": 1,
  "startDate": "2026-05-19",
  "endDate": "2026-05-25",
  "weeklyCalorieTarget": 14700,
  "weeklyProteinTarget": 630,
  "weeklyCarbTarget": 1715,
  "weeklyFatTarget": 490,
  "weeklyStrategy": "...",
  "aiPreparationTips": "...",
  "dailyPlans": [...]
}
```

---

## Troubleshooting

### Issue: "Ingredient not found"
- **Solution**: Ensure all ingredient IDs in LLM response match your database. Check ingredient list being sent to LLM.

### Issue: "LLM failed to generate plan"
- **Solution**: Check the reason in the error response. Adjust ingredients, allergies, or calories.

### Issue: Invalid JSON from LLM
- **Solution**: Add retry logic with slight prompt variations. LLMs sometimes hallucinate.

### Issue: Meals not appearing in response
- **Solution**: Ensure prompt includes all required meal types and ingredients list is not empty.

---

## Next Steps

1. ✅ Create services (PromptBuilder, LLMClient, PlanGeneration)
2. ✅ Create controller endpoint
3. ✅ Add dependencies
4. ✅ Configure application.yml
5. ✅ Test end-to-end
6. ✅ Deploy to production
7. ✅ Monitor LLM API costs

---

## References

- Prompt Template: `src/main/resources/prompts/weekly-diet-plan-with-meals.txt`
- Example Response: `WEEKLY_DIET_PLAN_EXAMPLE_RESPONSE.json`
- Full Guide: `WEEKLY_DIET_PLAN_PROMPT_GUIDE.md`


