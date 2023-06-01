package com.smart.commons.validate.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 验证异常
 * @author zhongming4762
 * 2023/5/25
 */
@Getter
public class ValidateException extends RuntimeException {

    private final List<ValidateErrorData> errorList;

    public ValidateException(Set<ConstraintViolation<Object>> validateResult) {
        this.errorList = validateResult.stream()
                .map(item -> new ValidateErrorData(item.getPropertyPath().toString(), item.getMessage()))
                .collect(Collectors.toList());
    }

    @Getter
    @AllArgsConstructor
    public static class ValidateErrorData {
        private String propertyPath;

        private String message;
    }
}
