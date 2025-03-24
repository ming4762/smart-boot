package com.smart.framework.ai.dify.constants;

import lombok.Getter;

/**
 * Errors
 * @author shizhongming
 * 2025/2/8 21:36
 * @since 5.0.0
 */
@Getter
public enum ErrorCodeEnum implements EnumValue {
    /**
     * 错误码
     */
    NOT_FOUND("404", "Not Found"),
    INVALID_PARAM("400", "传入参数异常"),
    APP_UNAVAILABLE("400", "App 配置不可用"),
    PROVIDER_NOT_INITIALIZE("400", "无可用模型凭据配置"),
    PROVIDER_QUOTA_EXCEEDED("400", "模型调用额度不足"),
    MODEL_CURRENTLY_NOT_SUPPORT("400", "当前模型不可用"),
    COMPLETION_REQUEST_ERROR("400", "文本生成失败");
    ;

    private final String code;

    private final String message;

    ErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取枚举值
     *
     * @return 枚举值
     */
    @Override
    public String getValue() {
        return this.code;
    }
}
