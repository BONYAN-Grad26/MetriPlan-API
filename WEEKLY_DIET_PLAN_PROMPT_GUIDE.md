# Weekly Diet Plan LLM Prompt - Complete Guide

## Overview
This prompt is designed to generate a complete weekly diet plan (7 days) with daily meals and ingredients in JSON format. The LLM will respond with a structured JSON containing:
- Weekly nutritional targets
- 7 daily plans (Monday-Sunday)
- Multiple meals per day
- Ingredients with IDs for each meal

## File Location
```
src/main/resources/prompts/weekly-diet-plan-with-meals.txt
```

## Key Features

### 1. **Structured JSON Output**
The LLM returns ONLY valid JSON with the following hierarchy:
```
WeeklyPlan
├── weekNumber
├── startDate / endDate
├── Weekly targets (calories, protein, carbs, fat)
├── weeklyStrategy
├── aiPreparationTips
└── days[] (7 days)
    └── day
        ├── dayOfWeek (1-7)
        ├── date
        ├── Daily targets
        ├── aiDailyTips
        └── meals[] (3-5 meals)
            └── meal
                ├── mealType (BREAKFAST, LUNCH, etc.)
                ├── name
                ├── description
                ├── preparationTime
                ├── preparationInstructions
                └── ingredients[] (multiple ingredients)
                    └── ingredient
                        ├── ingredientId (CRITICAL for DB lookup)
                        ├── ingredientName (exact match)
                        ├── quantity
                        ├── measurementUnit
                        └── nutrition (calories, protein, carbs, fat)
```

## Template Variables

Replace these placeholders when sending the prompt to LLM:

### User Profile Variables
- `{{userAge}}` - User's age in years
- `{{userGender}}` - MALE, FEMALE, OTHER
- `{{userWeight}}` - Weight in kg (decimal)
- `{{userHeight}}` - Height in cm (integer)

### Body Composition Variables
- `{{muscleMass}}` - Muscle mass in kg (nullable)
- `{{bodyFatPercentage}}` - Body fat % (nullable)
- `{{fatMass}}` - Fat mass in kg (calculated)
- `{{leanMass}}` - Lean mass in kg (calculated)
- `{{bodyFatCategory}}` - LOW, NORMAL, HIGH, VERY_HIGH

### Metabolic Variables
- `{{bmi}}` - BMI value (decimal)
- `{{bmiCategory}}` - UNDERWEIGHT, NORMAL, OVERWEIGHT, OBESE
- `{{tdee}}` - Total Daily Energy Expenditure (calories)

### Goal & Preferences Variables
- `{{dietGoal}}` - LOSE_WEIGHT, GAIN_MUSCLE, MAINTAIN_WEIGHT, IMPROVE_HEALTH
- `{{dietType}}` - NONE, VEGETARIAN, VEGAN, KETO, PALEO, MEDITERRANEAN, etc.
- `{{activityLevel}}` - SEDENTARY, LIGHTLY_ACTIVE, MODERATELY_ACTIVE, VERY_ACTIVE
- `{{targetWeight}}` - Target weight in kg (decimal, nullable)
- `{{medicalNotes}}` - Medical conditions/notes from user

### Nutrition Targets Variables
- `{{dailyCalories}}` - Daily calorie target (integer)
- `{{dailyProtein}}` - Daily protein target in grams
- `{{dailyCarbs}}` - Daily carbs target in grams
- `{{dailyFat}}` - Daily fat target in grams
- `{{dailyFiber}}` - Daily fiber target in grams
- `{{dailySugar}}` - Daily sugar limit in grams
- `{{waterGoal}}` - Daily water goal in ml

### Restrictions Variables
- `{{allergyList}}` - Comma-separated list of allergen names
  - Format: "Peanuts, Tree Nuts, Milk, Gluten"
  - Can be: "None" if no allergies

### Ingredients Variable
- `{{availableIngredientsList}}` - Pre-formatted list of all available ingredients
  - Format per ingredient:
    ```
    - ID: 1
    - Name: Chicken Breast
    - Category: PROTEIN
    - Calories (per 100g): 165
    - Protein (g/100g): 31.0
    - Carbs (g/100g): 0.0
    - Fat (g/100g): 3.6
    - Fiber (g/100g): 0.0
    - Sugar (g/100g): 0.0
    ```

### Plan Duration Variables
- `{{startDate}}` - Start date of week in yyyy-MM-dd format
- `{{endDate}}` - End date of week in yyyy-MM-dd format (7 days later)
- `{{weekNumber}}` - Week number (1, 2, 3, etc.)

## Java Implementation Example

### 1. Create a Prompt Service

```java
@Service
public class LLMPromptService {
    
    @Value("${app.llm.weekly-diet-plan-prompt-path}")
    private String weeklyDietPlanPromptPath;
    
    private final HealthMetricsRepository healthMetricsRepository;
    private final IngredientRepository ingredientRepository;
    private final AllergyRepository allergyRepository;
    
    public String buildWeeklyDietPlanPrompt(Long userId, LocalDate startDate, int weekNumber) {
        HealthMetrics metrics = healthMetricsRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Health metrics not found"));
        
        List<Ingredient> availableIngredients = ingredientRepository.findAllAvailable();
        List<Allergy> userAllergies = allergyRepository.findByUserId(userId);
        
        String promptTemplate = readPromptTemplate(weeklyDietPlanPromptPath);
        
        // Calculate derived values
        LocalDate endDate = startDate.plusDays(6);
        Double dailyCalories = (double) metrics.getTdee();
        
        // Build ingredient list
        String ingredientList = buildIngredientList(availableIngredients);
        
        // Build allergy list
        String allergyList = buildAllergyList(userAllergies);
        
        // Replace all variables
        String prompt = promptTemplate
            // User Profile
            .replace("{{userAge}}", metrics.getAge().toString())
            .replace("{{userGender}}", metrics.getGender().name())
            .replace("{{userWeight}}", metrics.getWeightKg().toString())
            .replace("{{userHeight}}", metrics.getHeightCm().toString())
            
            // Body Composition
            .replace("{{muscleMass}}", 
                metrics.getMuscleMassKg() != null ? metrics.getMuscleMassKg().toString() : "Not provided")
            .replace("{{bodyFatPercentage}}", 
                metrics.getFatPercentage() != null ? metrics.getFatPercentage().toString() : "Not provided")
            .replace("{{fatMass}}", 
                metrics.getFatMass() != null ? metrics.getFatMass().toString() : "Not provided")
            .replace("{{leanMass}}", 
                metrics.getLeanMass() != null ? metrics.getLeanMass().toString() : "Not provided")
            .replace("{{bodyFatCategory}}", 
                metrics.getBodyFatCategory() != null ? metrics.getBodyFatCategory() : "Not provided")
            
            // Metabolic
            .replace("{{bmi}}", metrics.getBmi().toString())
            .replace("{{bmiCategory}}", metrics.getBmiCategory())
            .replace("{{tdee}}", metrics.getTdee().toString())
            
            // Goals & Preferences
            .replace("{{dietGoal}}", metrics.getDietGoal().name())
            .replace("{{dietType}}", metrics.getDietType().name())
            .replace("{{activityLevel}}", metrics.getActivityLevel().name())
            .replace("{{targetWeight}}", 
                metrics.getTargetWeightKg() != null ? metrics.getTargetWeightKg().toString() : "Not set")
            .replace("{{medicalNotes}}", 
                metrics.getMedicalNotes() != null ? metrics.getMedicalNotes() : "None")
            
            // Nutrition Targets
            .replace("{{dailyCalories}}", dailyCalories.toString())
            .replace("{{dailyProtein}}", calculateDailyProtein(metrics).toString())
            .replace("{{dailyCarbs}}", calculateDailyCarbs(metrics).toString())
            .replace("{{dailyFat}}", calculateDailyFat(metrics).toString())
            .replace("{{dailyFiber}}", "30") // Recommended daily fiber
            .replace("{{dailySugar}}", "50")  // Maximum daily sugar
            .replace("{{waterGoal}}", "3000") // 3L water per day
            
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
    
    private String buildIngredientList(List<Ingredient> ingredients) {
        return ingredients.stream()
            .map(ingredient -> String.format(
                "- ID: %d\n" +
                "- Name: %s\n" +
                "- Category: %s\n" +
                "- Calories (per 100g): %f\n" +
                "- Protein (g/100g): %f\n" +
                "- Carbs (g/100g): %f\n" +
                "- Fat (g/100g): %f\n" +
                "- Fiber (g/100g): %f\n" +
                "- Sugar (g/100g): %f\n",
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
    
    private String buildAllergyList(List<Allergy> allergies) {
        if (allergies.isEmpty()) {
            return "None";
        }
        return allergies.stream()
            .map(Allergy::getAllergenName)
            .collect(Collectors.joining(", "));
    }
    
    private String readPromptTemplate(String path) {
        try {
            Resource resource = new ClassPathResource(path);
            return new String(Files.readAllBytes(Paths.get(resource.getURI())));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read prompt template", e);
        }
    }
    
    private Double calculateDailyProtein(HealthMetrics metrics) {
        // 1.6-2.2g per kg for muscle building, 1.2-1.6g for maintenance
        double proteinMultiplier = metrics.getDietGoal() == DietGoal.GAIN_MUSCLE ? 2.0 : 1.6;
        return metrics.getWeightKg() * proteinMultiplier;
    }
    
    private Double calculateDailyCarbs(HealthMetrics metrics) {
        // Carbs = (TDEE - protein_cals - fat_cals) / 4
        double proteinCals = calculateDailyProtein(metrics) * 4;
        double fatCals = calculateDailyFat(metrics) * 9;
        return (metrics.getTdee() - proteinCals - fatCals) / 4;
    }
    
    private Double calculateDailyFat(HealthMetrics metrics) {
        // 0.8-1.2g per kg body weight
        return metrics.getWeightKg() * 1.0;
    }
}
```

### 2. Parse LLM Response

```java
@Service
public class WeeklyPlanGenerationService {
    
    private final ObjectMapper objectMapper;
    private final LLMClient llmClient;
    private final WeeklyPlanRepository weeklyPlanRepository;
    private final DailyPlanRepository dailyPlanRepository;
    private final MealRepository mealRepository;
    private final MealIngredientRepository mealIngredientRepository;
    
    public WeeklyPlan generateWeeklyPlan(Long userId, LocalDate startDate, int weekNumber) {
        // 1. Build prompt
        String prompt = llmPromptService.buildWeeklyDietPlanPrompt(userId, startDate, weekNumber);
        
        // 2. Send to LLM (e.g., Gemini, GPT-4, Claude)
        String llmResponse = llmClient.generateContent(prompt);
        
        // 3. Parse JSON response
        JsonNode responseNode = objectMapper.readTree(llmResponse);
        
        if (responseNode.has("error")) {
            throw new PlanGenerationException("LLM failed to generate plan: " + responseNode.get("reason").asText());
        }
        
        // 4. Create WeeklyPlan entity
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
        
        // 5. Process daily plans
        List<DailyPlan> dailyPlans = new ArrayList<>();
        for (JsonNode dayNode : responseNode.get("days")) {
            DailyPlan dailyPlan = processDailyPlan(dayNode);
            dailyPlan.setWeeklyPlan(weeklyPlan);
            dailyPlans.add(dailyPlan);
        }
        weeklyPlan.setDailyPlans(dailyPlans);
        
        // 6. Save to database
        return weeklyPlanRepository.save(weeklyPlan);
    }
    
    private DailyPlan processDailyPlan(JsonNode dayNode) {
        DailyPlan dailyPlan = new DailyPlan();
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
        
        List<DailyPlanMeal> meals = new ArrayList<>();
        for (JsonNode mealNode : dayNode.get("meals")) {
            DailyPlanMeal dailyPlanMeal = processMeal(mealNode);
            dailyPlanMeal.setDailyPlan(dailyPlan);
            meals.add(dailyPlanMeal);
        }
        dailyPlan.setMeals(meals);
        
        return dailyPlan;
    }
    
    private DailyPlanMeal processMeal(JsonNode mealNode) {
        // 1. Find or create Meal
        Meal meal = findOrCreateMeal(mealNode);
        
        // 2. Create MealIngredients
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
        
        // 3. Create DailyPlanMeal
        DailyPlanMeal dailyPlanMeal = new DailyPlanMeal();
        dailyPlanMeal.setMeal(meal);
        dailyPlanMeal.setMealOrder(mealNode.get("order").asInt());
        
        return dailyPlanMeal;
    }
    
    private Meal findOrCreateMeal(JsonNode mealNode) {
        String mealName = mealNode.get("name").asText();
        
        return mealRepository.findByName(mealName)
            .orElseGet(() -> {
                Meal newMeal = new Meal();
                newMeal.setName(mealName);
                newMeal.setDescription(mealNode.get("description").asText());
                newMeal.setMealType(MealType.valueOf(mealNode.get("mealType").asText()));
                newMeal.setPreparationInstructions(mealNode.get("preparationInstructions").asText());
                newMeal.setPreparationTime(mealNode.get("preparationTime").asInt());
                
                return mealRepository.save(newMeal);
            });
    }
}
```

## Important Notes

### 1. **Ingredient ID Mapping**
- The LLM MUST include `ingredientId` in the response
- These IDs must match your database
- Format the ingredient list in the prompt with exact IDs

### 2. **Allergen Handling**
- Pass user allergies as a comma-separated string
- The LLM will avoid ingredients containing these allergens
- If no allergies, pass "None"

### 3. **Measurement Units**
- The LLM uses standard units: g, ml, kg, l, cup, tbsp, tsp, piece, oz, lb
- Choose the most practical unit for each ingredient

### 4. **Macronutrient Calculations**
- Ensure TDEE and macro targets are accurate
- The prompt enforces ±10% tolerance

### 5. **Error Handling**
- If LLM returns `{"error": "FAILED", "reason": "..."}`, regenerate with adjusted parameters
- Common reasons: Not enough ingredients, too strict restrictions

### 6. **Response Validation**
- Always validate JSON structure before saving to database
- Check that all ingredient IDs exist
- Verify date ranges are correct

## Configuration (application.yml)

```yaml
app:
  llm:
    weekly-diet-plan-prompt-path: "classpath:prompts/weekly-diet-plan-with-meals.txt"
```

## Testing the Prompt

Use this curl command to test with Gemini API:

```bash
curl -X POST "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=YOUR_API_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "contents": [{
      "parts": [{
        "text": "YOUR_PROMPT_HERE"
      }]
    }]
  }'
```

## Summary

This prompt ensures:
✅ Structured JSON output with all necessary details
✅ Ingredient IDs for database lookups
✅ Respect for allergies and dietary preferences
✅ Accurate macronutrient targeting
✅ Practical meal planning with 7 days of meals
✅ Clear preparation instructions
✅ Professional AI-generated tips

You can now extract ingredient IDs from the response and create your database entities accordingly!

