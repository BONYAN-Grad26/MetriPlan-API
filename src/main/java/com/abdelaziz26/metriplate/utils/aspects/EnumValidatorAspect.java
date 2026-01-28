package com.abdelaziz26.metriplate.utils.aspects;

import com.abdelaziz26.metriplate.utils.annotations.ValidateEnumValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.catalina.Contained;

public class EnumValidatorAspect implements ConstraintValidator<ValidateEnumValue, String> {

    private Class<? extends Enum<?>> enumClass;
    @Override
    public void initialize(ValidateEnumValue constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) return true;

        for (Enum<?> enumVal : enumClass.getEnumConstants()) {
            if (enumVal.name().equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }
}
