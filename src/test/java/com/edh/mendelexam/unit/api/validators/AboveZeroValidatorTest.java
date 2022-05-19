package com.edh.mendelexam.unit.api.validators;

import com.edh.mendelexam.api.validators.AboveZeroValidator;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class AboveZeroValidatorTest {

    AboveZeroValidator notZeroOrBelowValidation = new AboveZeroValidator();

    @Test
    void isValid_withValueOverZero_returnTrue() {
        assertThat(notZeroOrBelowValidation.isValid(10d, mock(ConstraintValidatorContext.class)))
                .isTrue();
    }

    @Test
    void isValid_withValueBelowZero_returnFalse() {
        assertThat(notZeroOrBelowValidation.isValid(-10d, mock(ConstraintValidatorContext.class)))
                .isFalse();
    }

    @Test
    void isValid_withValueEqualsZero_returnFalse() {
        assertThat(notZeroOrBelowValidation.isValid(0d, mock(ConstraintValidatorContext.class)))
                .isFalse();
    }
}
