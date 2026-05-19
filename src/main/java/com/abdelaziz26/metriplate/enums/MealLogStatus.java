package com.abdelaziz26.metriplate.enums;

public enum MealLogStatus {
    EATEN_AS_PLANNED,   // Followed the plan exactly
    EATEN_MODIFIED,     // Ate the planned meal but changed portion (portionMultiplier != 1.0)
    SUBSTITUTED,        // Replaced with a different food (substituteFood is populated)
    SKIPPED             // Did not eat this meal
}
