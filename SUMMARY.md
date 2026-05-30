# Weekly Diet Plan LLM Generation - Quick Summary

## What Has Been Created

I've created a complete LLM prompt and implementation guide for generating weekly diet plans with meals and ingredients. Here are all the files:

### 📄 Files Created

1. **weekly-diet-plan-with-meals.txt** (Prompt Template)
   - Location: `src/main/resources/prompts/weekly-diet-plan-with-meals.txt`
   - A comprehensive prompt that instructs the LLM to generate a complete 7-day weekly diet plan
   - Includes all placeholders for user metrics, available ingredients, allergies, and nutrition targets

2. **WEEKLY_DIET_PLAN_PROMPT_GUIDE.md** (Complete Guide)
   - Detailed explanation of the prompt structure
   - Template variables and how to populate them
   - Complete Java implementation examples for:
     - Building the prompt
     - Parsing LLM response
     - Saving to database
   - Usage instructions

3. **WEEKLY_DIET_PLAN_EXAMPLE_RESPONSE.json** (Example Response)
   - Real-world example of what the LLM should return
   - A complete 7-day meal plan with:
     - 28-32 total meals
     - Each meal with ingredients and ingredient IDs
     - Nutritional information per ingredient
     - Preparation instructions

4. **IMPLEMENTATION_STEPS.md** (Step-by-Step Guide)
   - How to implement the prompt in your app
   - Service layer implementations
   - Controller endpoints
   - Configuration setup
   - Testing examples

---

## The Prompt Structure

The prompt ensures the LLM responds with **JSON ONLY** containing:

```
WeeklyPlan (1 week)
├── Week targets (calories, protein, carbs, fat)
├── 7 Daily Plans (Monday-Sunday)
│   ├── Daily targets
│   ├── Daily tips
│   └── Meals (3-5 per day)
│       ├── Meal name & type
│       ├── Preparation instructions
│       └── Ingredients (with IDs for DB lookup)
│           ├── Ingredient ID (✅ CRITICAL for database)
│           ├── Ingredient name
│           ├── Quantity & unit
│           └── Nutrition info
```

---

## Key Features of the Prompt

✅ **Ingredient ID Mapping**
- LLM includes ingredient IDs in response
- You use these IDs to link to your database
- No name matching needed

✅ **Strict Format Rules**
- EXACTLY 7 days
- EXACTLY 1 week
- 3-5 meals per day
- Standardized measurement units
- Valid meal types only

✅ **Health & Safety**
- Respects user allergies
- Follows diet type (VEGAN, KETO, etc.)
- Matches calorie targets
- Balanced macronutrients

✅ **Professional Content**
- Clear preparation instructions
- Realistic preparation times
- AI-generated strategy & tips
- No repetition of meals on same day

---

## How to Use It

### 1. **Build the Prompt**
```java
String prompt = promptBuilder.buildPrompt(
    userMetrics,      // HealthMetrics from DB
    ingredients,      // Available ingredients from DB
    userAllergies,    // Allergies from DB
    startDate,        // e.g., 2026-05-19
    weekNumber        // e.g., 1
);
```

### 2. **Send to LLM**
```java
String response = llmClient.generateContent(prompt);
// Sends to Gemini, GPT-4, Claude, etc.
```

### 3. **Parse JSON Response**
```java
JsonNode json = objectMapper.readTree(response);
WeeklyPlan plan = parseWeeklyPlan(json);
```

### 4. **Extract Ingredient IDs & Save**
```
For each Meal:
  For each Ingredient:
    - Get ingredientId from JSON
    - Look up Ingredient by ID in your DB
    - Create MealIngredient with quantity & nutrition
    - Save to database
```

---

## Expected LLM Response Structure

```json
{
  "weekNumber": 1,
  "startDate": "2026-05-19",
  "endDate": "2026-05-25",
  "weeklyCalorieTarget": 14700,
  "weeklyProteinTarget": 630,
  "weeklyCarbTarget": 1715,
  "weeklyFatTarget": 490,
  "days": [
    {
      "dayOfWeek": 1,
      "date": "2026-05-19",
      "meals": [
        {
          "mealType": "BREAKFAST",
          "name": "Protein Oatmeal",
          "ingredients": [
            {
              "ingredientId": 1,          // ← YOU USE THIS TO FIND INGREDIENT IN DB
              "ingredientName": "Oats",
              "quantity": 80,
              "measurementUnit": "g"
            }
          ]
        }
      ]
    }
  ]
}
```

---

## Template Variables to Replace

When building the prompt, replace these placeholders with actual user data:

### User Profile
- `{{userAge}}`, `{{userGender}}`, `{{userWeight}}`, `{{userHeight}}`

### Body Composition
- `{{muscleMass}}`, `{{bodyFatPercentage}}`, `{{fatMass}}`, `{{leanMass}}`

### Metabolic
- `{{bmi}}`, `{{bmiCategory}}`, `{{tdee}}`

### Goals
- `{{dietGoal}}`, `{{dietType}}`, `{{activityLevel}}`, `{{targetWeight}}`

### Nutrition Targets
- `{{dailyCalories}}`, `{{dailyProtein}}`, `{{dailyCarbs}}`, `{{dailyFat}}`

### Restrictions
- `{{allergyList}}` - Comma-separated allergen names

### Ingredients
- `{{availableIngredientsList}}` - Formatted list with IDs and nutrition

### Dates
- `{{startDate}}`, `{{endDate}}`, `{{weekNumber}}`

---

## Ingredient List Format

You must format ingredients as:

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

- ID: 2
- Name: Oats
- Category: GRAINS
- Calories (per 100g): 389
...
```

---

## Valid Enum Values

### Meal Types (mealType)
- BREAKFAST
- LUNCH
- DINNER
- SNACK
- DESSERT
- PRE_WORKOUT
- POST_WORKOUT

### Measurement Units (measurementUnit)
- g (grams)
- ml (milliliters)
- kg (kilograms)
- l (liters)
- cup (cups)
- tbsp (tablespoons)
- tsp (teaspoons)
- piece (pieces)
- oz (ounces)
- lb (pounds)

---

## Database Operations

After parsing the LLM response:

```
1. Create WeeklyPlan entity
   ├─ Save weeklyCalorieTarget, strategy, tips, etc.
   └─ Save to database

2. For each day (7 total):
   ├─ Create DailyPlan
   ├─ Set daily targets & tips
   │
   └─ For each meal (3-5 per day):
      ├─ Find or create Meal
      │
      └─ For each ingredient:
         ├─ Get ingredientId from JSON
         ├─ Find Ingredient by ID (already exists in DB)
         ├─ Create MealIngredient
         ├─ Set quantity, unit, nutrition
         └─ Save to database
```

---

## Error Handling

If LLM returns an error:

```json
{
  "error": "FAILED",
  "reason": "Not enough variety in ingredients provided"
}
```

Retry with:
- More diverse ingredients
- Adjusted calorie targets
- Different allergies/restrictions

---

## Files Reference

| File | Purpose |
|------|---------|
| `weekly-diet-plan-with-meals.txt` | The actual prompt template |
| `WEEKLY_DIET_PLAN_PROMPT_GUIDE.md` | Complete implementation guide with code |
| `WEEKLY_DIET_PLAN_EXAMPLE_RESPONSE.json` | Example of expected LLM response |
| `IMPLEMENTATION_STEPS.md` | Step-by-step implementation instructions |
| `SUMMARY.md` | This file |

---

## Next Steps

1. Copy the prompt template to your resources folder ✅ Done
2. Create the `WeeklyDietPlanPromptBuilder` service
3. Create the `LLMClient` service (for your LLM provider)
4. Create the `WeeklyPlanGenerationService`
5. Create the API controller endpoint
6. Add configuration to `application.yml`
7. Test end-to-end
8. Deploy!

---

## Example API Call

```bash
POST /api/diet-plans/generate-weekly-plan
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json

{
  "startDate": "2026-05-19",
  "weekNumber": 1
}

Response:
{
  "id": 1,
  "weekNumber": 1,
  "startDate": "2026-05-19",
  "endDate": "2026-05-25",
  "weeklyCalorieTarget": 14700,
  "weeklyProteinTarget": 630,
  "dailyPlans": [...]
}
```

---

## Key Takeaways

🎯 **The LLM generates:** Complete 7-day meal plans with ingredient IDs
📦 **You extract:** Ingredient IDs from the JSON
🔗 **You link:** Ingredients to your database using the IDs
💾 **You save:** Meals, MealIngredients, DailyPlans, WeeklyPlan

✅ The prompt is comprehensive, strict, and production-ready!


