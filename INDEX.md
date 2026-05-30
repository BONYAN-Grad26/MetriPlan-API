# LLM Weekly Diet Plan Generation - Complete Index

## 📋 Quick Navigation

Start here and follow the sections in order based on your needs.

---

## 🚀 Quick Start (5 minutes)

**If you just need to understand the concept:**
1. Read: **[SUMMARY.md](./SUMMARY.md)** - High-level overview (5 min)
2. View: **[WEEKLY_DIET_PLAN_EXAMPLE_RESPONSE.json](./WEEKLY_DIET_PLAN_EXAMPLE_RESPONSE.json)** - See example LLM response

---

## 🔧 Complete Implementation (1-2 hours)

**If you want to implement this in your app:**

1. **Read the prompts guide first:**
   - 📖 **[WEEKLY_DIET_PLAN_PROMPT_GUIDE.md](./WEEKLY_DIET_PLAN_PROMPT_GUIDE.md)**
     - Variables to replace
     - Java implementation examples
     - How to parse responses
     - Testing guide

2. **Then follow implementation steps:**
   - 📝 **[IMPLEMENTATION_STEPS.md](./IMPLEMENTATION_STEPS.md)**
     - Step 1-8: Services, controllers, configuration
     - Code snippets ready to use
     - Database operations
     - Testing examples

3. **Use the actual prompt template:**
   - 💡 **[weekly-diet-plan-with-meals.txt](./src/main/resources/prompts/weekly-diet-plan-with-meals.txt)**
     - Located in: `src/main/resources/prompts/weekly-diet-plan-with-meals.txt`
     - This is what you send to the LLM

4. **Test with example response:**
   - ✅ **[WEEKLY_DIET_PLAN_EXAMPLE_RESPONSE.json](./WEEKLY_DIET_PLAN_EXAMPLE_RESPONSE.json)**
     - Use this to test your parsing logic
     - Contains a full 7-day meal plan with all ingredients

---

## 📚 File Descriptions

### 1. **SUMMARY.md** (8 KB)
**What:** Quick reference guide
**Contains:**
- Overview of what was created
- Expected JSON structure
- Template variables
- Enum values
- Database operations flow
- Error handling

**Read time:** 5-10 minutes
**Best for:** Understanding the big picture

---

### 2. **WEEKLY_DIET_PLAN_PROMPT_GUIDE.md** (18 KB)
**What:** Comprehensive implementation guide with code examples
**Contains:**
- Prompt structure explanation
- Template variable descriptions
- Complete Java service code:
  - `LLMPromptService` - Building prompts
  - `WeeklyPlanGenerationService` - Parsing responses
- Configuration instructions
- Testing examples
- Troubleshooting guide

**Read time:** 30-40 minutes
**Best for:** Understanding how to integrate with your app

---

### 3. **IMPLEMENTATION_STEPS.md** (22 KB)
**What:** Step-by-step implementation guide
**Contains:**
- Step 1: Prompt Builder Service
- Step 2: LLM Client Service
- Step 3: Plan Parsing & Persistence Service
- Step 4: Controller Endpoint
- Step 5: Configuration
- Step 6: Dependencies
- Step 7: Database Entities
- Step 8: Testing
- Troubleshooting

**Read time:** 45-60 minutes
**Best for:** Following along while coding

---

### 4. **weekly-diet-plan-with-meals.txt** (8.1 KB)
**What:** The actual LLM prompt template
**Location:** `src/main/resources/prompts/weekly-diet-plan-with-meals.txt`
**Contains:**
- System prompt (instructions to LLM)
- Rules for meal types, units, macros
- Placeholders for user data
- JSON schema the LLM must follow
- Validation checklist

**How to use:**
```java
// 1. Read this file
String template = Files.readString(Paths.get(...));

// 2. Replace placeholders with actual user data
String prompt = template
  .replace("{{userAge}}", "28")
  .replace("{{dailyCalories}}", "2100")
  // ... more replacements ...

// 3. Send to LLM
String response = llmClient.generateContent(prompt);
```

---

### 5. **WEEKLY_DIET_PLAN_EXAMPLE_RESPONSE.json** (40 KB)
**What:** Example JSON response from the LLM
**Contains:**
- Complete 7-day weekly plan
- 28 meals (4 per day)
- Each meal with multiple ingredients
- Ingredient IDs, quantities, nutrition info
- Preparation instructions
- Daily tips and strategy

**How to use:**
```java
// 1. Parse this file for testing
JsonNode example = objectMapper.readTree(jsonFile);

// 2. Test your parsing logic
WeeklyPlan plan = parseWeeklyPlan(example);

// 3. Verify the structure matches your entities
assert plan.getDailyPlans().size() == 7;
assert plan.getDailyPlans().get(0).getMeals().size() == 4;
```

**Real-world example of:**
- Week 1 of a muscle-building diet (2100 cal/day)
- Mix of protein sources (chicken, beef, turkey, fish, lamb)
- Variety of meals (breakfast, lunch, dinner, snacks)
- Realistic preparation times and instructions
- Complete ingredient lists with IDs

---

## 🎯 Entity Relationships

Understanding the data model:

```
WeeklyPlan (1)
  ↓
  └─→ DailyPlan (7 per week)
       ↓
       └─→ DailyPlanMeal (3-5 per day)
            ↓
            └─→ Meal (shared across plans)
                 ↓
                 └─→ MealIngredient (multiple per meal)
                      ↓
                      └─→ Ingredient (from your database)
```

**Key:** The LLM returns `ingredientId`, which you use to link to your existing Ingredient records.

---

## 🔑 Critical Variables Explained

### Input Variables (to replace in prompt)

| Variable | Source | Format |
|----------|--------|--------|
| `{{userAge}}` | HealthMetrics.age | Integer |
| `{{dailyCalories}}` | HealthMetrics.tdee | Integer (e.g., 2100) |
| `{{dietType}}` | HealthMetrics.dietType | Enum name (VEGAN, KETO, etc.) |
| `{{allergyList}}` | Allergy table | Comma-separated (Peanuts, Milk, etc.) |
| `{{availableIngredientsList}}` | Ingredient table | Formatted list with IDs |
| `{{startDate}}` | Request parameter | yyyy-MM-dd (2026-05-19) |
| `{{weekNumber}}` | Request parameter | Integer (1, 2, etc.) |

### Output Variables (from LLM response)

| Variable | Type | Usage |
|----------|------|-------|
| `weekNumber` | Integer | Identify which week |
| `startDate` / `endDate` | Date strings | Track week period |
| `days[].dayOfWeek` | 1-7 | Monday = 1, Sunday = 7 |
| `days[].meals[].mealType` | Enum | BREAKFAST, LUNCH, DINNER, etc. |
| `meals[].ingredients[].ingredientId` | Long | Link to Ingredient table |
| `ingredients[].quantity` | Double | Amount needed |
| `ingredients[].measurementUnit` | String | g, ml, cup, etc. |

---

## 💾 Database Save Flow

```
1. Parse LLM response JSON
   ↓
2. Create WeeklyPlan entity
   - Set weekNumber, startDate, endDate
   - Set weekly targets & strategy
   - SAVE to database
   ↓
3. For each of 7 days:
   - Create DailyPlan entity
   - Set daily targets & tips
   - SAVE to database
   ↓
4. For each meal in day:
   - Find or create Meal entity
   - SAVE to database (if new)
   ↓
5. For each ingredient in meal:
   - Get ingredientId from JSON (e.g., 5)
   - Query Ingredient table: SELECT * WHERE id = 5
   - Create MealIngredient entity
   - Link meal ↔ ingredient
   - Set quantity, unit, nutrition
   - SAVE to database
```

---

## ✅ Checklist for Implementation

### Phase 1: Preparation
- [ ] Read SUMMARY.md
- [ ] Read WEEKLY_DIET_PLAN_PROMPT_GUIDE.md
- [ ] Review WEEKLY_DIET_PLAN_EXAMPLE_RESPONSE.json
- [ ] Copy weekly-diet-plan-with-meals.txt to your resources folder

### Phase 2: Service Layer
- [ ] Create WeeklyDietPlanPromptBuilder service
- [ ] Create LLMClient service (for your LLM provider)
- [ ] Create WeeklyPlanGenerationService
- [ ] Verify all imports and dependencies

### Phase 3: Controller
- [ ] Create DietPlanController endpoint
- [ ] Add request/response DTOs
- [ ] Add error handling

### Phase 4: Configuration
- [ ] Add LLM credentials to application.yml
- [ ] Add prompt path configuration
- [ ] Set up environment variables

### Phase 5: Testing
- [ ] Test prompt building
- [ ] Test LLM API connectivity
- [ ] Test JSON parsing
- [ ] Test database persistence
- [ ] End-to-end test

### Phase 6: Deployment
- [ ] Review security
- [ ] Set rate limits
- [ ] Monitor LLM API costs
- [ ] Deploy to staging
- [ ] Deploy to production

---

## 🤝 Integration Points

### With Your Existing Code

#### HealthMetricsService
- Already fetches user metrics
- You'll use this data to build the prompt

#### IngredientRepository
- Already stores all ingredients with IDs
- You'll pass this to the prompt builder

#### AllergyRepository
- Already stores user allergies
- You'll format these for the LLM

#### WeeklyPlanRepository
- You'll implement this to save generated plans

#### MealRepository & MealIngredientRepository
- You'll implement these to save meals and ingredients

---

## 🧪 Testing Strategy

### Unit Tests
```java
// Test 1: Prompt building works
testPromptBuilder() 
  - Verifies all placeholders replaced
  - Checks ingredient list format
  - Validates date format

// Test 2: JSON parsing works
testJsonParsing()
  - Parse example response
  - Verify structure matches entities
  - Check meal counts

// Test 3: Database operations
testDatabasePersistence()
  - Save WeeklyPlan
  - Save DailyPlans
  - Verify relationships
```

### Integration Tests
```java
// Full flow test
testEndToEnd()
  - Build prompt
  - Mock LLM response (use example JSON)
  - Parse response
  - Save to database
  - Query and verify
```

---

## 🚨 Important Notes

### ⚠️ Prompt Usage
- The prompt is **strict and comprehensive**
- It instructs the LLM to return **ONLY JSON**
- It ensures **exactly 7 days** are returned
- It enforces **ingredient validation**

### ⚠️ Ingredient IDs
- The LLM **must include ingredientId** in every ingredient
- These IDs **must match your database**
- If IDs don't exist, the save will fail
- **Solution:** Validate ingredients exist before sending prompt

### ⚠️ Error Handling
- If LLM returns `{"error": "FAILED", "reason": "..."}`, retry with different parameters
- Common failures: Not enough ingredients, too strict allergies
- **Always have fallback data** for testing

### ⚠️ API Costs
- Each prompt generation calls the LLM API
- This costs money (typically $0.001 - $0.01 per request)
- **Monitor your API usage** and set limits
- Consider **caching responses** for same user/week

---

## 📞 Support & Troubleshooting

### Issue: Prompt building fails
**Cause:** Missing or null values in HealthMetrics
**Fix:** Ensure HealthMetrics is complete before building prompt

### Issue: LLM returns invalid JSON
**Cause:** LLM hallucinated or deviated from prompt
**Fix:** Add retry logic with slight variations

### Issue: Ingredients not found in response
**Cause:** LLM hallucinated ingredient names
**Fix:** Ensure ingredient list in prompt is clear and complete

### Issue: Parsing fails
**Cause:** Unexpected JSON structure
**Fix:** Use ObjectMapper with DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES = false

---

## 📖 Additional Resources

### Prompt Engineering Tips
- Keep instructions clear and unambiguous
- Use structured examples
- Specify exact output format
- Add validation rules

### LLM Providers
- **Google Gemini:** Vertex AI API
- **OpenAI:** GPT-4, GPT-4 Turbo
- **Anthropic:** Claude 3
- **Local:** Ollama, LLaMA 2

### Best Practices
- Version your prompts
- Track LLM API costs
- Monitor response quality
- A/B test different prompts

---

## 🎉 You're All Set!

Now you have:

✅ A production-ready prompt template
✅ Complete implementation guide with code examples
✅ Real-world example responses
✅ Step-by-step setup instructions
✅ Testing strategies
✅ Troubleshooting guide

**Next Step:** Start implementing the services from IMPLEMENTATION_STEPS.md!

---

**Last Updated:** May 19, 2026
**Files:** 5 total (1 prompt, 1 JSON, 3 guides)
**Total Size:** ~96 KB
**Implementation Time:** 1-2 hours
**Status:** ✅ Ready for Production


