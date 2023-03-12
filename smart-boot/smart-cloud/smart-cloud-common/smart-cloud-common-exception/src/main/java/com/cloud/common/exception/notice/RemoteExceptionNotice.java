package com.cloud.common.exception.notice;

import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.commons.core.utils.ExceptionUtils;
import com.smart.commons.core.utils.IpUtils;
import com.smart.module.api.system.SysExceptionApi;
import com.smart.module.api.system.dto.SysExceptionSaveDTO;
import com.smart.starter.exception.notice.AbstractCommonExcludeExceptionNotice;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
     * @param e           异常信息
     * @param exceptionNo 异常编号
     * @param user        用户
     * @param request     请求信息
     */
    @Override
    protected void doNotice(@NonNull Exception e, long exceptionNo, RestUserDetails user, @NonNull HttpServletRequest request) {
        try {
            SysExceptionSaveDTO dto = SysExceptionSaveDTO.builder()
                    .id(exceptionNo)
                    .exceptionMessage(e.toString())
                    .stackTrace(ExceptionUtils.stackTraceToString(e))
                    .requestIp(IpUtils.getIpAddr(request))
                    .serverIp(InetAddress.getLocalHost().getHostAddress())
                    .requestPath(request.getServletPath())
                    .operateUserId(user == null ? null : user.getUserId())
                    .operationBy(user == null ? null : user.getFullName())
                    .build();
            this.sysExceptionApi.saveException(dto);
        } catch (UnknownHostException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
