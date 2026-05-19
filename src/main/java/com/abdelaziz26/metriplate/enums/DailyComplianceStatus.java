package com.abdelaziz26.metriplate.enums;

public enum DailyComplianceStatus {
    PLANNED,           // Day not yet started
    IN_PROGRESS,       // Partially logged during the day
    FULLY_COMPLIANT,   // All meals eaten as planned, targets met
    PARTIALLY_COMPLIANT, // Some meals followed, minor deviations
    MISSED,            // User did not log / skipped the day entirely
    REST_DAY           // Intentional day off (not counted against adherence)
}
