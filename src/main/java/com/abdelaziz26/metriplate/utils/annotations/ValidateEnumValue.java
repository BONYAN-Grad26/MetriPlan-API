package com.abdelaziz26.metriplate.utils.annotations;

import com.abdelaziz26.metriplate.utils.aspects.EnumValidatorAspect;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValidatorAspect.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateEnumValue {
    String message() default "Value is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * The Enum class to validate against
     */
    Class<? extends Enum<?>> enumClass();
}
