package com.smart.auth.extensions.sms.provider;

import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.service.AuthCache;
import com.smart.module.api.message.SmartMessageApi;
import com.smart.module.api.message.dto.SmsSendDTO;
import com.smart.module.api.message.parameter.RemoteSmsSendParameter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author zhongming4762
 * 2023/6/6
 */
@Slf4j
public class DefaultSmsCreateValidateProviderImpl implements SmsCreateValidateProvider {

    private static final String PREFIX = "smart_auth_sms_login";

    private final AuthCache<String, Object> authCache;

    private final SmartMessageApi smartMessageApi;

    private final AuthProperties authProperties;

    public DefaultSmsCreateValidateProviderImpl(AuthCache<String, Object> authCache, SmartMessageApi smartMessageApi, AuthProperties authProperties) {
        this.authCache = authCache;
        this.smartMessageApi = smartMessageApi;
        this.authProperties = authProperties;
    }


    /**
     * 创建验证码
     *
     * @param phone 手机号
     * @return 验证码
     */
    @Override
    public String create(@NonNull String phone) {
        AuthProperties.SmsProperties smsProperties = this.authProperties.getSms();
        // 生成6为验证码
        String code = RandomStringUtils.randomNumeric(6);
        // 发送短信
        LinkedHashMap<String, String> templateParameter = new LinkedHashMap<>(1);
        templateParameter.put("code", code);
        SmsSendDTO sendResult = this.smartMessageApi.sendSms(
                RemoteSmsSendParameter.builder()
                        .phoneNumberList(List.of(phone))
                        .signName(smsProperties.getSignName())
                        .template(smsProperties.getTemplate())
                        .templateParameter(templateParameter)
                        .build()
        );
        log.debug("登录发送短信成功，响应数据：{}", sendResult);
        // 存储验证码到缓存，有效期5分钟
        this.authCache.put(this.getCodeKey(phone), code, Duration.ofMinutes(5));
        return code;
    }

    /**
     * 验证验证码是否有效
     *
     * @param phone 手机号
     * @param code  验证码
     * @return 验证结果
     */
    @Override
    public boolean validate(@NonNull String phone, String code) {
        // 获取验证码
        String codeKey = this.getCodeKey(phone);
        String cacheCode = (String) this.authCache.get(codeKey);
        if (StringUtils.isBlank(cacheCode) || !StringUtils.equals(cacheCode, code)) {
            return false;
        }
        // 删除缓存
        this.authCache.remove(codeKey);
        return true;
    }

    /**
     * 获取验证码的key
     * @param phone 手机号
     * @return 验证码
     */
    protected String getCodeKey(String phone) {
        return String.format("%s_%s", PREFIX, phone);
    }
}
