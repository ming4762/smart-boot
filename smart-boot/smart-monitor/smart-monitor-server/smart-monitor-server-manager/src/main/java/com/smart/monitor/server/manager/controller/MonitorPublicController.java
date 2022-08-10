package com.smart.monitor.server.manager.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.auth.core.annotation.TempToken;
import com.smart.monitor.server.manager.model.MonitorClientLogPO;
import com.smart.monitor.server.manager.pojo.dto.LogDownloadDTO;
import com.smart.monitor.server.manager.service.MonitorClientLogService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2022/3/4
 * @since 1.1.0
 */
@Controller
@RequestMapping("public/monitor")
public class MonitorPublicController {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");

    private final MonitorClientLogService monitorClientLogService;

    public MonitorPublicController(MonitorClientLogService monitorClientLogService) {
        this.monitorClientLogService = monitorClientLogService;
    }

    @SneakyThrows(IOException.class)
    @Operation(summary = "下载日志")
    @GetMapping("downloadFilterUser")
    @TempToken(resource = "monitor:client:log")
    public void downloadFilterUser(LogDownloadDTO parameter, HttpServletResponse response) {
        LambdaQueryWrapper<MonitorClientLogPO> query = new QueryWrapper<MonitorClientLogPO>().lambda()
                .orderByAsc(MonitorClientLogPO :: getTimestamp);
        if (parameter.getLevel() != null) {
            query.eq(MonitorClientLogPO :: getLevel, parameter.getLevel());
        }
        if (parameter.getApplicationCode() != null) {
            query.eq(MonitorClientLogPO :: getApplicationCode, parameter.getApplicationCode());
        }
        if (parameter.getClientId() != null) {
            query.eq(MonitorClientLogPO :: getClientId, parameter.getClientId());
        }
        if (parameter.getStartTime() != null) {
            query.ge(MonitorClientLogPO :: getTimestamp, parameter.getStartTime());
        }
        if (parameter.getEndTime() != null) {
            query.le(MonitorClientLogPO :: getTimestamp, parameter.getEndTime());
        }
        List<MonitorClientLogPO> logList = this.monitorClientLogService.list(query);
        String fileName = DATE_TIME_FORMATTER.format(LocalDateTime.now()) + ".log";
        response.setHeader("Content-Disposition", "attachment;filename=" + new String (fileName.getBytes (StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
        response.setCharacterEncoding (StandardCharsets.UTF_8.name());
        for (MonitorClientLogPO log : logList) {
            response.getWriter().write(log.getLogText());
            response.getWriter().write("\n");
        }
        response.getWriter().flush();
    }
}
