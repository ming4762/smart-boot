package com.smart.db.generator.controller;

import com.smart.auth.core.annotation.TempToken;
import com.smart.db.generator.model.DbCodeTemplatePO;
import com.smart.db.generator.model.DbConnectionPO;
import com.smart.db.generator.pojo.dto.DbCreateDicDTO;
import com.smart.db.generator.service.DbCodeTemplateService;
import com.smart.db.generator.service.DbConnectionService;
import com.smart.db.generator.service.DbDictGeneratorService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author ShiZhongMing
 * 2021/8/5 15:03
 * @since 1.0
 */
@RequestMapping("public/db")
@Controller
public class DbPublicController {

    private final DbCodeTemplateService templateService;

    private final DbDictGeneratorService databaseGeneratorService;

    private final DbConnectionService databaseConnectionService;

    public DbPublicController(DbCodeTemplateService templateService, DbDictGeneratorService databaseGeneratorService, DbConnectionService databaseConnectionService) {
        this.templateService = templateService;
        this.databaseGeneratorService = databaseGeneratorService;
        this.databaseConnectionService = databaseConnectionService;
    }

    /**
     * 创建数据库字典
     * @param parameter 参数
     * @param response HttpServletResponse
     */
    @GetMapping("createDic")
    @TempToken(resource = "db:connection:createDic")
    public void createDic(DbCreateDicDTO parameter, HttpServletResponse response) throws IOException {
        // 查询模板信息
        DbCodeTemplatePO template = this.templateService.getById(parameter.getTemplateId());
        Assert.notNull(template, "找不到数据库字典模板");
        DbConnectionPO databaseConnection = this.databaseConnectionService.getById(parameter.getConnectionId());
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(databaseConnection.getConnectionName() + "." + template.getLanguage(), StandardCharsets.UTF_8.name()));
        Assert.notNull(databaseConnection, "找不到数据库连接信息");
        this.databaseGeneratorService.createDatabaseDic(databaseConnection.createConnectionConfig(), response.getOutputStream(), template.getName(), template.getTemplate());
    }
}
