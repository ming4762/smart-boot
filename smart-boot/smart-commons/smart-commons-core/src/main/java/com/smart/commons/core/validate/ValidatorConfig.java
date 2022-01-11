package com.smart.commons.core.validate;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * 参数验证器配置类
 * @author jackson
 * 2020/3/12 9:54 下午
 */
@Configuration
public class ValidatorConfig {

    /**
     * 创建验证器
     * 设置只验证第一个
     * @return 参数校验
     */
    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure()
                .addProperty( "hibernate.validator.fail_fast", "true" )
                .buildValidatorFactory();
        return validatorFactory.getValidator();
    }
}
