package com.edh.mendelexam.api.validators;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AboveZeroValidator implements ConstraintValidator<AboveZero, Double> {
    @Override
    public boolean isValid(Double aDouble, ConstraintValidatorContext constraintValidatorContext) {
        return aDouble > 0;
    }
}
