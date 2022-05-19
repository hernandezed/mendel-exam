package com.edh.mendelexam.api.validators;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AboveZeroValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AboveZero {
    String message() default "Invalid value cannot be zero or below";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
