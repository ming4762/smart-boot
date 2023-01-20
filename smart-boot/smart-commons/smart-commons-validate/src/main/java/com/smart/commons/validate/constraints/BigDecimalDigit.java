package com.smart.commons.validate.constraints;

import com.smart.commons.validate.constraintvalidators.BigDecimalDigitValidator;

import javax.validation.Constraint;
import java.lang.annotation.*;

/**
 * 验证BigDecimal位数
 * @author shizhongming
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Constraint(validatedBy = BigDecimalDigitValidator.class)
public @interface BigDecimalDigit {

    /**
     * 小数位
     * @return 小数位数
     */
    int scale();

    /**
     * 总位数
     * @return 总位数
     */
    int precision();

    String message() default "";
}
