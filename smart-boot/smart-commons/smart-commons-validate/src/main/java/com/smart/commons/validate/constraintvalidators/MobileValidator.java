package com.smart.commons.validate.constraintvalidators;

import com.smart.commons.validate.constraints.Mobile;
import com.smart.commons.validate.utils.ValidatorUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 手机号校验器
 * TODO 扩展：支持自定义正则表达式校验
 * @author shizhongming
 * 2020/6/2 1:53 下午
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!StringUtils.hasText(value)) {
            return true;
        }
        return ValidatorUtils.checkMobile(value);
    }
}
