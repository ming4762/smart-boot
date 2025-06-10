package com.smart.module.system.exception;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.commons.core.utils.ExceptionUtils;
import com.smart.framework.commons.core.utils.IpUtils;
import com.smart.framework.exception.notice.AbstractCommonExcludeExceptionNotice;
import com.smart.module.system.model.SysExceptionPO;
import com.smart.module.system.service.SysExceptionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;

/**
 * 将异常信息保存到数据库
 * @author ShiZhongMing
 * 2022/6/10
 * @since 3.0.0
 */
@Component("dbExceptionNotice")
@Slf4j
public class DbExceptionNotice extends AbstractCommonExcludeExceptionNotice {

    private final SysExceptionService sysExceptionService;

    public DbExceptionNotice(SysExceptionService sysExceptionService) {
        this.sysExceptionService = sysExceptionService;
    }

    @Override
    protected void doNotice(@NonNull Exception e, long exceptionNo, @NonNull HttpServletRequest request) {
        // 异常信息保存到数据库
        RestUserDetails user = AuthUtils.getCurrentUser();
        try {
            SysExceptionPO sysException = SysExceptionPO.builder()
                    .id(exceptionNo)
                    .exceptionMessage(e.toString())
                    .stackTrace(ExceptionUtils.throwableToString(e, true))
                    .requestIp(IpUtils.getIpAddr(request))
                    .serverIp(InetAddress.getLocalHost().getHostAddress())
                    .requestPath(request.getServletPath())
                    .operateUserId(user == null ? null : user.getUserId())
                    .operationBy(user == null ? null : user.getFullName())
                    .createTime(ZonedDateTime.now())
                    .build();
            this.sysExceptionService.save(sysException);
        } catch (UnknownHostException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
