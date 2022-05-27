package com.smart.commons.validate.constraintvalidators;

import com.smart.commons.validate.constraints.Contain;
import com.smart.commons.validate.enums.IValidateEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 是否包含验证器
 * @author shizhongming
 * 2020/6/2 4:41 下午
 */
public class ContainValidator implements ConstraintValidator<Contain, Object> {

    private String[] allow;

    private Class<? extends IValidateEnum> allowClass;

    @Override
    public void initialize(Contain constraintAnnotation) {
        this.allow = constraintAnnotation.allow();
        this.allowClass = constraintAnnotation.allowClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return true;
        }
        return this.checkAllowClass(value) && this.checkAllow(value);
    }

    /**
     * 判断是否在allow里
     * @param value value
     * @return 是否在allow里
     */
    private boolean checkAllow(Object value) {
        final List<String> allowList = Arrays.asList(allow);
        return allowList.isEmpty() || allowList.contains(value.toString());
    }

    /**
     * 检查是否在包含的枚举内
     * @param value value
     * @return 是否在包含的枚举内
     */
    private boolean checkAllowClass(Object value) {
        if (Objects.equals(this.allowClass, IValidateEnum.class)) {
            return true;
        }
        IValidateEnum[] enums = this.allowClass.getEnumConstants();
        return Arrays.stream(enums).anyMatch(item -> Objects.equals(item.getValidateValue(), value));
    }
}
