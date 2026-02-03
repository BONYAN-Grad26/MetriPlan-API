package com.abdelaziz26.metriplate.utils.ai;

import lombok.*;

@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class MealPromptContext {
    private String mealType;

    private Double mealCalories;

    private String ingredients;
    private String allergies;
}
