package com.smart.framework.auth.extensions.wechat.constants;

import com.smart.framework.commons.core.http.IHttpStatus;
import com.smart.framework.commons.core.i18n.I18nMessage;
import lombok.Getter;

/**
 * 微信登录状态码枚举
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-02 18:59
 * @since 5.0.0
 */
@Getter
public enum WechatLoginCodeEnum implements IHttpStatus, I18nMessage {
    MP_QRCODE_EXPIRED(40029, "auth.wechat.qrcode.mp_qrcode_expired", "二维码已过期"),
    MP_QRCODE_NO_VALID(40030, "auth.wechat.qrcode.mp_qrcode_no_valid", "二维码未扫描");
    ;

    private final Integer code;

    private final String i18nCode;

    private final String message;

    WechatLoginCodeEnum(Integer code, String i18nCode, String message) {
        this.code = code;
        this.i18nCode = i18nCode;
        this.message = message;
    }

    /**
     * 获取状态信息
     *
     * @return 状态信息
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 获取I18N key
     *
     * @return key
     */
    @Override
    public String getI18nCode() {
        return this.i18nCode;
    }
}
