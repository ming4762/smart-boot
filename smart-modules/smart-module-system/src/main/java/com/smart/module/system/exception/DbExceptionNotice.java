package com.smart.module.system.exception;

import com.smart.framework.commons.core.utils.ExceptionUtils;
import com.smart.framework.exception.notice.AbstractCommonExcludeExceptionNotice;
import com.smart.framework.exception.pojo.dto.ExceptionNoticeDTO;
import com.smart.module.api.system.SysExceptionApi;
import com.smart.module.api.system.dto.SysExceptionSaveDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * 将异常信息保存到数据库
 * @author ShiZhongMing
 * 2022/6/10
 * @since 3.0.0
 */
@Component("dbExceptionNotice")
@Slf4j
@RequiredArgsConstructor
public class DbExceptionNotice extends AbstractCommonExcludeExceptionNotice {

    private final SysExceptionApi sysExceptionApi;

    @Override
    protected void doNotice(@NonNull ExceptionNoticeDTO exceptionData) {
        // 异常信息保存到数据库
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
