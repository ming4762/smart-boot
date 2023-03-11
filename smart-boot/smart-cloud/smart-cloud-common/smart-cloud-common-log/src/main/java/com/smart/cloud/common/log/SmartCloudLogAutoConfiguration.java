package com.smart.cloud.common.log;

import com.smart.cloud.api.system.feign.RemoteSysLogApi;
import com.smart.cloud.common.log.handler.RemoteLogHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongming4762
 * 2023/3/11
 */
@Configuration(proxyBeanMethods = false)
public class SmartCloudLogAutoConfiguration {

    /**
     * 创建日志处理器  使用远程调用方式保存日志
     * @param logApi 系统日志远程调用接口
     * @return RemoteLogHandler
     */
    @Bean
    public RemoteLogHandler remoteLogHandler(RemoteSysLogApi logApi) {
        return new RemoteLogHandler(logApi);
    }
}
