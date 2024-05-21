package com.smart.message.dingtalk;

import com.smart.dingtalk.api.UserApi;
import com.smart.dingtalk.api.WorkNoticeApi;
import com.smart.message.dingtalk.sender.SmartDingdingWorkNoticeSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongming4762
 * 2023/5/26
 */
@Configuration(proxyBeanMethods = false)
public class SmartMessageDingdingAutoConfiguration {

    /**
     * 钉钉工作通知
     * @return SmartDingdingWorkNoticeSender
     */
    @Bean
    public SmartDingdingWorkNoticeSender dingdingWorkNoticeSender(WorkNoticeApi workNoticeApi, UserApi userApi) {
        return new SmartDingdingWorkNoticeSender(workNoticeApi, userApi);
    }
}
