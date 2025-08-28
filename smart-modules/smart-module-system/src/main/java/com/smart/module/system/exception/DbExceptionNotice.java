package com.smart.module.system.exception;

import com.smart.framework.commons.core.utils.ExceptionUtils;
import com.smart.framework.commons.core.utils.IpUtils;
import com.smart.framework.exception.notice.AbstractCommonExcludeExceptionNotice;
import com.smart.module.api.system.SysExceptionApi;
import com.smart.module.api.system.dto.SysExceptionSaveDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
    protected void doNotice(@NonNull Exception e, long exceptionNo, @NonNull HttpServletRequest request) {
        // 异常信息保存到数据库
        try {
            SysExceptionSaveDTO dto = SysExceptionSaveDTO.builder()
                    .id(exceptionNo)
                    .exceptionMessage(e.toString())
                    .stackTrace(ExceptionUtils.throwableToString(e, true))
                    .requestIp(IpUtils.getIpAddr(request))
                    .serverIp(InetAddress.getLocalHost().getHostAddress())
                    .requestPath(request.getServletPath())
                    .build();
            this.sysExceptionApi.saveException(dto);
        } catch (UnknownHostException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
