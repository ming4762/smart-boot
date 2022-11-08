package com.smart.system.exception;

import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.commons.core.utils.ExceptionUtils;
import com.smart.commons.core.utils.IpUtils;
import com.smart.starter.exception.notice.AbstractCommonExcludeExceptionNotice;
import com.smart.system.model.SysExceptionPO;
import com.smart.system.service.SysExceptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

/**
 * 将异常信息保存到数据库
 * @author ShiZhongMing
 * 2022/6/10
 * @since 3.0.0
 */
@Component
@Slf4j
public class DbExceptionNotice extends AbstractCommonExcludeExceptionNotice {

    private final SysExceptionService sysExceptionService;

    public DbExceptionNotice(SysExceptionService sysExceptionService) {
        this.sysExceptionService = sysExceptionService;
    }

    @Override
    protected void doNotice(@NonNull Exception e, long exceptionNo, RestUserDetails user, @NonNull HttpServletRequest request) {
        // 异常信息保存到数据库
        try {
            SysExceptionPO sysException = SysExceptionPO.builder()
                    .id(exceptionNo)
                    .exceptionMessage(e.toString())
                    .stackTrace(ExceptionUtils.stackTraceToString(e))
                    .requestIp(IpUtils.getIpAddr(request))
                    .serverIp(InetAddress.getLocalHost().getHostAddress())
                    .requestPath(request.getServletPath())
                    .operateUserId(user == null ? null : user.getUserId())
                    .createTime(LocalDateTime.now())
                    .build();
            this.sysExceptionService.save(sysException);
        } catch (UnknownHostException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
