package com.smart.cloud.api.auth.feign;

import com.smart.cloud.common.core.constants.CloudServiceNameConstants;
import com.smart.commons.core.captcha.dto.CaptchaGenerateDTO;
import com.smart.commons.core.captcha.dto.CaptchaGenerateParameter;
import com.smart.commons.core.captcha.dto.CaptchaValidateParameter;
import com.smart.module.api.auth.AuthCaptchaApi;
import com.smart.module.api.auth.constants.SmartAuthApiUrlConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 认证模块验证码feign调用接口
 * @author zhongming4762
 * 2023/6/27
 */
@FeignClient(value = CloudServiceNameConstants.AUTH_SERVICE, contextId = "remoteAuthCaptchaApi")
public interface RemoteAuthCaptchaApi extends AuthCaptchaApi {

    /**
     * 创建验证码
     *
     * @param parameter 参数
     * @return 生成的验证码
     */
    @Override
    @PostMapping(SmartAuthApiUrlConstants.AUTH_CAPTCHA_GENERATE)
    CaptchaGenerateDTO generate(CaptchaGenerateParameter parameter);

    /**
     * 验证码验证
     *
     * @param parameter 参数
     * @return 验证码生成的临时token
     */
    @Override
    @PostMapping(SmartAuthApiUrlConstants.AUTH_CAPTCHA_VALIDATE)
    boolean validate(CaptchaValidateParameter parameter);

}
