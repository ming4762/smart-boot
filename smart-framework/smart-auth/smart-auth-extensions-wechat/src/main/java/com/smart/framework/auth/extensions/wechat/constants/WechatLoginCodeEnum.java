package com.smart.framework.auth.extensions.wechat.constants;

import com.smart.framework.auth.core.i18n.AuthI18nMessage;
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
    MP_QRCODE_EXPIRED(401, 40029, "auth.wechat.qrcode.mp_qrcode_expired", "二维码已过期"),
    MP_QRCODE_NO_VALID(401, 40030, "auth.wechat.qrcode.mp_qrcode_no_valid", "二维码未扫描"),
    USER_NOT_BOUND(401, 40031, AuthI18nMessage.WECHAT_USER_NOT_BOND.getI18nCode(), "用户未绑定")
    ;

    private final Integer code;

    private final Integer subCode;

    private final String i18nCode;

    private final String message;

    WechatLoginCodeEnum(Integer code, Integer subCode, String i18nCode, String message) {
        this.code = code;
        this.subCode = subCode;
        this.i18nCode = i18nCode;
        this.message = message;
    }

    /**
     * 子编码
     *
     * @return 子编码
     */
    @Override
    public Integer getSubCode() {
        return this.subCode;
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
