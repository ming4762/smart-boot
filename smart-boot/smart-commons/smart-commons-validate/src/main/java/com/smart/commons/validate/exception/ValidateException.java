package com.smart.commons.validate.exception;

import jakarta.validation.ConstraintViolation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
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
                .toList();
    }

    @Getter
    @AllArgsConstructor
    public static class ValidateErrorData implements Serializable {
        private String propertyPath;

        private String message;
    }

    @Override
    public String getMessage() {
        return this.errorList.stream()
                .map(ValidateErrorData::getMessage)
                .collect(Collectors.joining(";"));
    }
}
