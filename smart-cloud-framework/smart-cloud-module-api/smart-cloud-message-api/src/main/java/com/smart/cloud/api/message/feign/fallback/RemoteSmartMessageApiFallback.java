package com.smart.cloud.api.message.feign.fallback;

import com.smart.cloud.api.message.feign.RemoteSmartMessageApi;
import com.smart.module.api.message.dto.MessageSendDTO;
import com.smart.module.api.message.dto.SmsSendDTO;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;
import com.smart.module.api.message.parameter.RemoteSmsSendParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shizhongming
 * 2025/6/16 9:49
 * @since 5.0.0
 */
@Component
@Slf4j
public class RemoteSmartMessageApiFallback implements FallbackFactory<RemoteSmartMessageApi> {
    @Override
    public RemoteSmartMessageApi create(Throwable cause) {
        return new RemoteSmartMessageApi() {

            private void errorLog() {
                log.error("RemoteSmartMessageApiFallback", cause);
            }

            @Override
            public SmsSendDTO sendSms(RemoteSmsSendParameter parameter) {
                this.errorLog();
                return null;
            }

            @Override
            public List<MessageSendDTO> send(RemoteMessageSendParameter parameter) {
                this.errorLog();
                return List.of();
            }
        };
    }
}
