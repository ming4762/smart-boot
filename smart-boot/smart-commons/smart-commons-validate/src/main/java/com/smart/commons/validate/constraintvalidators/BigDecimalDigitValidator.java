package com.smart.commons.validate.constraintvalidators;

import com.smart.commons.validate.constraints.BigDecimalDigit;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * 验证BigDecimal的位数
 * @author shizhongming
 * 2022/12/6
 */
public class BigDecimalDigitValidator implements ConstraintValidator<BigDecimalDigit, BigDecimal> {

    private Integer scale;

    private Integer precision;

    /**
     * Initializes the validator in preparation for
     * {@link #isValid(Object, ConstraintValidatorContext)} calls.
     * The constraint annotation for a given constraint declaration
     * is passed.
     * <p>
     * This method is guaranteed to be called before any use of this instance for
     * validation.
     * <p>
     * The default implementation is a no-op.
     *
     * @param constraintAnnotation annotation instance for a given constraint declaration
     */
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
        if (value.precision() != this.precision) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        BigDecimal bigDecimal = new BigDecimal("125.68");
        System.out.println(bigDecimal);
    }
}
