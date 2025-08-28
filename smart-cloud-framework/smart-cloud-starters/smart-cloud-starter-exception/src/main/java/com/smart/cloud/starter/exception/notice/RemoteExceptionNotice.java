package com.smart.cloud.starter.exception.notice;

import com.smart.framework.commons.core.utils.ExceptionUtils;
import com.smart.framework.exception.notice.AbstractCommonExcludeExceptionNotice;
import com.smart.framework.exception.pojo.dto.ExceptionNoticeDTO;
import com.smart.module.api.system.SysExceptionApi;
import com.smart.module.api.system.dto.SysExceptionSaveDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.net.InetAddress;

/**
 * @author zhongming4762
 * 2023/3/12 21:21
 */
@Slf4j
public class RemoteExceptionNotice extends AbstractCommonExcludeExceptionNotice {

    private final SysExceptionApi sysExceptionApi;

    public RemoteExceptionNotice(SysExceptionApi sysExceptionApi) {
        this.sysExceptionApi = sysExceptionApi;
    }

    /**
     * 进行通知
     *
     * @param exceptionData 异常信息
     */
    @Override
    protected void doNotice(@NonNull ExceptionNoticeDTO exceptionData) {
        try {
            SysExceptionSaveDTO dto = SysExceptionSaveDTO.builder()
                    .id(exceptionData.getExceptionNo())
                    .exceptionMessage(exceptionData.getException().toString())
                    .stackTrace(ExceptionUtils.throwableToString(exceptionData.getException(), true))
                    .requestIp(exceptionData.getRequestIp())
                    .serverIp(InetAddress.getLocalHost().getHostAddress())
                    .requestPath(exceptionData.getRequestPath())
                    .build();
            this.sysExceptionApi.saveException(dto);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
