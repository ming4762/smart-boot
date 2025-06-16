package com.smart.cloud.api.auth.feign.fallback;

import com.smart.cloud.api.auth.feign.RemoteAuthCaptchaApi;
import com.smart.framework.commons.core.captcha.dto.CaptchaGenerateDTO;
import com.smart.framework.commons.core.captcha.dto.CaptchaGenerateParameter;
import com.smart.framework.commons.core.captcha.dto.CaptchaValidateParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author shizhongming
 * 2025/6/16 9:42
 * @since 5.0.0
 */
@Component
@Slf4j
public class RemoteAuthCaptchaApiFallback implements FallbackFactory<RemoteAuthCaptchaApi> {
    @Override
    public RemoteAuthCaptchaApi create(Throwable cause) {
        return new RemoteAuthCaptchaApi() {

            private void errorLog() {
                log.error("RemoteAuthCaptchaApiFallback", cause);
            }

            @Override
            public CaptchaGenerateDTO generate(CaptchaGenerateParameter parameter) {
                this.errorLog();
                return null;
            }

            @Override
            public boolean validate(CaptchaValidateParameter parameter) {
                this.errorLog();
                return false;
            }
        };
    }
}
