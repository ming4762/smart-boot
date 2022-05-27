package com.smart.commons.validate.constraints;

import com.smart.commons.validate.constraintvalidators.ContainValidator;
import com.smart.commons.validate.enums.IValidateEnum;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 判断是否包含验证器
 * @author shizhongming
 * 2020/6/2 4:39 下午
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Constraint(validatedBy = ContainValidator.class)
public @interface Contain {

    String message() default "";

    String[] allow() default {};

    Class<? extends IValidateEnum> allowClass() default IValidateEnum.class;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
