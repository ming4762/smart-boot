package com.smart.cloud.api.message.feign.fallback;

import com.smart.cloud.api.message.feign.FeignSmartMessageApi;
import com.smart.module.api.message.dto.MessageSendResult;
import com.smart.module.api.message.dto.SmsSendResult;
import com.smart.module.api.message.parameter.RemoteSmsSendParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author shizhongming
 * 2025/6/16 9:49
 * @since 5.0.0
 */
@Component
@Slf4j
public class RemoteSmartMessageApiFallback implements FallbackFactory<FeignSmartMessageApi> {
    @Override
    public FeignSmartMessageApi create(Throwable cause) {
        return new FeignSmartMessageApi() {

            private void errorLog() {
                log.error("RemoteSmartMessageApiFallback", cause);
            }

            @Override
            public SmsSendResult sendSms(RemoteSmsSendParameter parameter) {
                this.errorLog();
                return null;
            }

            @Override
            public List<MessageSendResult> send(Map<String,Object> parameter) {
                this.errorLog();
                return List.of();
            }
        };
    }
}
