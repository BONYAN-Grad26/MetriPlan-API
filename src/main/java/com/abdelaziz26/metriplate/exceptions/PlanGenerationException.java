package com.abdelaziz26.metriplate.exceptions;

public class PlanGenerationException extends Exception{
    public PlanGenerationException(String message) {
        super(message);
    }
    public PlanGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
