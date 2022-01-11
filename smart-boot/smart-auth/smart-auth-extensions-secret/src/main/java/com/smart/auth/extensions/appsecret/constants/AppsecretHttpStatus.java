package com.smart.auth.extensions.appsecret.constants;

import com.smart.commons.core.http.IHttpStatus;
import com.smart.commons.core.i18n.I18nMessage;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
public enum AppsecretHttpStatus implements IHttpStatus, I18nMessage {
    /**
     * 开放API认证错误信息（含国际化）
     */
    APP_ID_SECRET_ERROR(40001, "auth.app.secret.app_id_secret_error", "app id or app secret error"),
    APP_IP_NOT_IN_WHITE_LIST(40164, "auth.app.secret.white_list_error", "ip not in white list"),
    APP_SECRET_PARAMETER_DEFECT(40051, "auth.app.secret.parameter_defect", "app secret parameter defect"),
    APP_SECRET_VALIDATE_TIMESTAMP_ERROR(40052, "auth.app.secret.validate_timestamp_error", "timestamp validate error"),
    APP_SECRET_VALIDATE_NONCESTR_ERROR(40053, "auth.app.secret.validate_noncestr_error", "noncestr validate error"),
    APP_SECRET_VALIDATE_URL_ERROR(40054, "auth.app.secret.validate_url_error", "url validate error"),
    APP_SECRET_VALIDATE_TOKEN_ERROR(40055, "auth.app.secret.validate_token_error", "access token validate error"),
    APP_SECRET_VALIDATE_SIGNATURE_ERROR(40056, "auth.app.secret.validate_signature_error", "signature validate error")
    ;


    private final Integer code;

    private final String i18nCode;

    private final String defaultMessage;

    AppsecretHttpStatus(Integer code, String i18nCode, String defaultMessage) {
        this.code = code;
        this.i18nCode = i18nCode;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.defaultMessage;
    }

    @Override
    public String getI18nCode() {
        return this.i18nCode;
    }

    @Override
    public String defaultMessage() {
        return this.defaultMessage;
    }
}
