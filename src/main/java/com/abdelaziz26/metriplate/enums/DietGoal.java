package com.abdelaziz26.metriplate.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum DietGoal {
    LOSE_WEIGHT,
    GAIN_MUSCLE,
    MAINTAIN_WEIGHT,
    IMPROVE_HEALTH
}

