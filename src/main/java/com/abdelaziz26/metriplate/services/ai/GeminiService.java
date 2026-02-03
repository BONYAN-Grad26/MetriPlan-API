package com.abdelaziz26.metriplate.services.ai;

import com.abdelaziz26.metriplate.dtos.plan.DayDTO;
import com.abdelaziz26.metriplate.dtos.plan.DietPlanDTO;
import com.abdelaziz26.metriplate.dtos.plan.MealDTO;
import com.abdelaziz26.metriplate.dtos.plan.WeekDTO;
import com.abdelaziz26.metriplate.entities.DietPlan;
import com.abdelaziz26.metriplate.entities.Goal;
import com.abdelaziz26.metriplate.entities.User;
import com.abdelaziz26.metriplate.exceptions.PlanGenerationException;
import com.abdelaziz26.metriplate.repositories.GoalRepository;
import com.abdelaziz26.metriplate.repositories.UserRepository;
import com.abdelaziz26.metriplate.responses.Gemini.GeminiDietPlanResponse;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.security.SecurityContextService;
import com.abdelaziz26.metriplate.services.plan.PlanService;
import com.abdelaziz26.metriplate.utils.ai.PromptBuilder;
import com.abdelaziz26.metriplate.utils.mappers.DietPlanMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatOptions;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.Set;

@Component
@Slf4j
public class GeminiService implements AiService {
    @Override
    public Result<DietPlanDTO, Error> generateAndSaveDietPlan() throws PlanGenerationException {
        return null;
    }

    //private static final Set<String> VALID_MEAL_TYPES = Set.of(
    //        "BREAKFAST", "LUNCH", "DINNER", "SNACK", "DESSERT", "PRE_WORKOUT", "POST_WORKOUT"
    //);
//
    //private final PromptBuilder promptBuilder;
    //private final ObjectMapper objectMapper;
    //private final ChatClient chatClient;
    //private final SecurityContextService securityContextService;
    //private final PlanService planService;
    //private final DietPlanMapper dietPlanMapper;
//
    //public GeminiService(ChatClient.Builder chatClient,
    //                     PromptBuilder promptBuilder,
    //                     UserRepository userRepository,
    //                     GoalRepository goalRepository, SecurityContextService securityContextService, PlanService planService, DietPlanMapper dietPlanMapper, DietPlanMapper dietPlanMapper1) {
    //    this.chatClient = chatClient.build();
    //    this.securityContextService = securityContextService;
    //    this.planService = planService;
    //    this.dietPlanMapper = dietPlanMapper1;
    //    this.objectMapper = new ObjectMapper();
    //    this.promptBuilder =  promptBuilder;
    //}
//
    //@Override
    //@Transactional
    //public Result<DietPlanDTO, Error> generateAndSaveDietPlan() throws PlanGenerationException {
//
    //    User user = securityContextService.getCurrentUser().orElse(null);
//
    //    if (user == null) {
    //        throw new PlanGenerationException("User is null");
    //    }
//
    //    Goal goal = user.getGoals().get(0);
//
    //    // 2. Build the prompt
    //    String promptText = promptBuilder.build(user.getId());
//
    //    log.info("Calling Gemini AI for user {}", user.getId());
    //    String jsonResponse = chatClient.prompt(promptText)
    //            .options(VertexAiGeminiChatOptions.builder()
    //                    .responseMimeType("application/json")
    //                    .temperature(0.1)
    //                    .build())
    //            .call()
    //            .content();
//
    //    GeminiDietPlanResponse response = objectMapper.readValue(jsonResponse, GeminiDietPlanResponse.class);
//
    //    validateResponse(response);
//
    //    if ("FAILED".equals(response.getStatus())) {
    //        throw new PlanGenerationException("AI generation failed: " + response.getReason());
    //    }
//
//
    //    DietPlan plan = planService.persistPlans(response.getDietPlan(), user, goal);
    //    return Result.CreateSuccessResult(dietPlanMapper.toDto(plan));
    //}
//
    //private void validateResponse(GeminiDietPlanResponse response) throws PlanGenerationException {
    //    // Check status
    //    if ("FAILED".equals(response.getStatus())) {
    //        throw new PlanGenerationException(
    //                "Gemini returned FAILED – reason: " +
    //                        (response.getReason() != null ? response.getReason() : "INVALID_CONSTRAINTS"));
    //    }
//
    //    if (response.getDietPlan() == null) {
    //        throw new PlanGenerationException("'dietPlan' key missing in Gemini response");
    //    }
//
    //    DietPlanDTO plan = response.getDietPlan();
//
    //    // Validate weeks
    //    if (plan.getWeeks() == null || plan.getWeeks().size() != 2) {
    //        throw new PlanGenerationException(
    //                "dietPlan.weeks must be an array of size 2, got " +
    //                        (plan.getWeeks() != null ? plan.getWeeks().size() : "null"));
    //    }
//
    //    // Validate each week
    //    for (int w = 0; w < plan.getWeeks().size(); w++) {
    //        WeekDTO week = plan.getWeeks().get(w);
//
    //        if (week.getDays() == null || week.getDays().size() != 7) {
    //            throw new PlanGenerationException(
    //                    "Week " + (w + 1) + " must have exactly 7 days, got " +
    //                            (week.getDays() != null ? week.getDays().size() : "null"));
    //        }
//
    //        // Validate each day
    //        for (int d = 0; d < week.getDays().size(); d++) {
    //            DayDTO day = week.getDays().get(d);
//
    //            if (day.getMeals() == null || day.getMeals().isEmpty()) {
    //                throw new PlanGenerationException(
    //                        "Week " + (w + 1) + ", day " + (d + 1) + " has no meals");
    //            }
//
    //            // Validate each meal
    //            for (int m = 0; m < day.getMeals().size(); m++) {
    //                MealDTO meal = day.getMeals().get(m);
//
    //                // Validate meal type
    //                if (!VALID_MEAL_TYPES.contains(meal.getMealType())) {
    //                    throw new PlanGenerationException(
    //                            "Invalid mealType '" + meal.getMealType() + "' at week " + (w + 1) +
    //                                    ", day " + (d + 1) + ", meal " + (m + 1));
    //                }
//
    //                // Validate ingredients
    //                if (meal.getIngredients() == null || meal.getIngredients().isEmpty()) {
    //                    throw new PlanGenerationException(
    //                            "Meal '" + meal.getName() + "' has no ingredients (week " +
    //                                    (w + 1) + ", day " + (d + 1) + ")");
    //                }
    //            }
    //        }
    //    }
    //}
//
    //private static String stripFences(String raw) {
    //    if (raw == null) return "";
    //    raw = raw.trim();
    //    if (raw.startsWith("```json")) raw = raw.substring(7);
    //    else if (raw.startsWith("```"))  raw = raw.substring(3);
    //    if (raw.endsWith("```"))         raw = raw.substring(0, raw.length() - 3);
    //    return raw.trim();
    //}
}
