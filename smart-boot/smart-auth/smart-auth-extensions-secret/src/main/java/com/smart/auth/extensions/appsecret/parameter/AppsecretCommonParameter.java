package com.smart.auth.extensions.appsecret.parameter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;


/**
 * 开放接口请求通用参数
 * @author ShiZhongMing
 * @since 1.0
 */
@Getter
@Setter
public class AppsecretCommonParameter {

    private String appId;

    private String timestamp;

    private String noncestr;

    private String signature;

    public static AppsecretCommonParameter createFromRequest(HttpServletRequest request) {
        AppsecretCommonParameter parameter = new AppsecretCommonParameter();
        parameter.setTimestamp(request.getParameter("timestamp"));
        parameter.setNoncestr(request.getParameter("noncestr"));
        parameter.setSignature(request.getParameter("signature"));
        parameter.setAppId(request.getParameter("appId"));
        return parameter;
    }
}
