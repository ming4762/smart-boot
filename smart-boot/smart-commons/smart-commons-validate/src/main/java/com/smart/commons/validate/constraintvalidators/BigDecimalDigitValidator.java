package com.smart.commons.validate.constraintvalidators;

import com.smart.commons.validate.constraints.BigDecimalDigit;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

/**
 * 验证BigDecimal的位数
 * @author shizhongming
 * 2022/12/6
 */
public class BigDecimalDigitValidator implements ConstraintValidator<BigDecimalDigit, BigDecimal> {

    private Integer scale;

    private Integer precision;

    @Override
    public void initialize(BigDecimalDigit constraintAnnotation) {
        this.scale = constraintAnnotation.scale();
        this.precision = constraintAnnotation.precision();
    }

    /**
     * Implements the validation logic.
     * The state of {@code value} must not be altered.
     * <p>
     * This method can be accessed concurrently, thread-safety must be ensured
     * by the implementation.
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return {@code false} if {@code value} does not pass the constraint
     */
    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        value = value.stripTrailingZeros();
        if (value.scale() != this.scale) {
            return false;
        }
        return value.precision() == this.precision;
    }
}
