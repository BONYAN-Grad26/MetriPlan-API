package com.abdelaziz26.metriplate.utils;

import com.abdelaziz26.metriplate.dtos.metrics.NutritionCalcResult;
import com.abdelaziz26.metriplate.enums.ActivityLevel;
import com.abdelaziz26.metriplate.enums.DietGoal;
import com.abdelaziz26.metriplate.enums.Gender;


public final class NutritionCalculator {

    private NutritionCalculator() {}


    public static double calculateBmi(double weightKg, double heightCm) {
        double heightM = heightCm / 100.0;
        return Math.round((weightKg / (heightM * heightM)) * 10.0) / 10.0;
    }

    public static String categorizeBmi(double bmi) {
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25.0) return "Normal";
        if (bmi < 30.0) return "Overweight";
        return "Obese";
    }


    // the approximate no of calories u burn a day
    public static int calculateTdee(
            double weightKg,
            double heightCm,
            int age,
            Gender gender,
            ActivityLevel activityLevel
    ) {
        double bmr = (10 * weightKg) + (6.25 * heightCm) - (5 * age);
        bmr += (gender == Gender.MALE) ? 5 : -161;
        return (int) Math.round(bmr * activityMultiplier(activityLevel));
    }

    public static int adjustCaloriesForGoal(int tdee, DietGoal goal) {
        return switch (goal) {
            case LOSE_WEIGHT     -> tdee - 500;
            case GAIN_MUSCLE     -> tdee + 300;
            case MAINTAIN_WEIGHT -> tdee;
            case IMPROVE_HEALTH  -> tdee;
        };
    }



    public static double calculateFatMass(double weightKg, double fatPercentage) {
        return Math.round((weightKg * fatPercentage / 100.0) * 10.0) / 10.0;
    }

    public static double calculateLeanMass(double weightKg, double fatPercentage) {
        return Math.round((weightKg - calculateFatMass(weightKg, fatPercentage)) * 10.0) / 10.0;
    }

    public static String categorizeBodyFat(double fatPercentage, Gender gender) {
        if (gender == Gender.MALE) {
            if (fatPercentage < 6)  return "Essential Fat";
            if (fatPercentage < 14) return "Athletic";
            if (fatPercentage < 18) return "Fitness";
            if (fatPercentage < 25) return "Acceptable";
            return "Obese";
        } else {
            // FEMALE and OTHER use the female scale as a safe default
            if (fatPercentage < 14) return "Essential Fat";
            if (fatPercentage < 21) return "Athletic";
            if (fatPercentage < 25) return "Fitness";
            if (fatPercentage < 32) return "Acceptable";
            return "Obese";
        }
    }

    public static NutritionCalcResult calculateAll(
            double weightKg,
            double heightCm,
            int age,
            Gender gender,
            ActivityLevel activityLevel,
            DietGoal goal,
            Double fatPercentage // optional
    ) {

        double bmi = calculateBmi(weightKg, heightCm);
        String bmiCategory = categorizeBmi(bmi);

        int tdee = calculateTdee(weightKg, heightCm, age, gender, activityLevel);
        int calorieTarget = adjustCaloriesForGoal(tdee, goal);

        Double fatMass = null;
        Double leanMass = null;
        String bodyFatCategory = null;

        if (fatPercentage != null) {
            fatMass = calculateFatMass(weightKg, fatPercentage);
            leanMass = calculateLeanMass(weightKg, fatPercentage);
            bodyFatCategory = categorizeBodyFat(fatPercentage, gender);
        }

        return NutritionCalcResult.builder()
                .bmi(bmi)
                .bmiCategory(bmiCategory)
                .tdee(tdee)
                .calorieTarget(calorieTarget)
                .fatMass(fatMass)
                .leanMass(leanMass)
                .bodyFatCategory(bodyFatCategory)
                .build();
    }

    private static double activityMultiplier(ActivityLevel level) {
        return switch (level) {
            case SEDENTARY         -> 1.2;
            case LIGHTLY_ACTIVE    -> 1.375;
            case MODERATELY_ACTIVE -> 1.55;
            case VERY_ACTIVE       -> 1.725;
        };
    }
}