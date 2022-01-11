package com.smart.db.generator.service.impl;

import com.smart.db.analysis.executor.DatabaseExecutor;
import com.smart.db.analysis.executor.DbExecutorProvider;
import com.smart.db.analysis.pojo.bo.TableViewBO;
import com.smart.db.analysis.pool.model.DbConnectionConfig;
import com.smart.db.generator.engine.TemplateEngine;
import com.smart.db.generator.engine.data.TemplateElement;
import com.smart.db.generator.pojo.dto.DatabaseTemplateModel;
import com.smart.db.generator.service.DbDictGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author shizhongming
 * 2020/7/3 9:36 上午
 */
@Service
@Slf4j
public class DatabaseGeneratorServiceImpl implements DbDictGeneratorService {

    private static final String DEFAULT_DIC_TEMPLATE = "template/databaseDic.ftl";

    private final TemplateEngine templateEngine;

    private final DbExecutorProvider dbExecutorProvider;

    public DatabaseGeneratorServiceImpl(TemplateEngine templateEngine, DbExecutorProvider dbExecutorProvider) {
        this.templateEngine = templateEngine;
        this.dbExecutorProvider = dbExecutorProvider;
    }

    /**
     * 创建数据库字典
     * @param databaseConnection 数据库连接信息
     * @param outputStream 输出流
     */
    @Override
    public void createDatabaseDic(@NonNull DbConnectionConfig databaseConnection, @NonNull OutputStream outputStream) {
        // 获取默认的模板信息
        final TemplateElement defaultTemplateElement = new TemplateElement(DEFAULT_DIC_TEMPLATE, null);
        this.createDatabaseDic(databaseConnection, outputStream, defaultTemplateElement);
    }

    /**
     * 创建数据库字典
     * @param databaseConnection 数据库连接信息
     * @param outputStream 输出流
     * @param templateElement 模板信息
     */
    @Override
    public void createDatabaseDic(@NonNull DbConnectionConfig databaseConnection, @NonNull OutputStream outputStream, @NonNull TemplateElement templateElement) {

        this.templateEngine.processToOutputStream(this.getDatabaseDicModel(databaseConnection), outputStream, templateElement);
    }

    /**
     * 创建数据库字段
     * @param config 数据库连接信息
     * @param outputStream 输出流
     * @param templateName 模板名字
     * @param templateValue 模板内容
     */
    @Override
    public void createDatabaseDic(@NonNull DbConnectionConfig config, @NonNull OutputStream outputStream, @NonNull String templateName, @NonNull String templateValue) {
        this.templateEngine.processTemplate(this.getDatabaseDicModel(config), templateName, templateValue, new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
    }

    private DatabaseTemplateModel getDatabaseDicModel(@NonNull DbConnectionConfig config) {
        // 获取数据库执行器
        final DatabaseExecutor databaseExecutor = this.dbExecutorProvider.getDatabaseExecutor(config);
        // 获取所有数据
        final List<TableViewBO> tableList = databaseExecutor.listTable(config, null);
        final Long columnSize = tableList.stream().map(item -> (item.getPrimaryKeyList() == null ? 0 : item.getPrimaryKeyList().size()) + (item.getBaseColumnList() == null ? 0 : item.getBaseColumnList().size())).mapToLong(Integer::longValue).sum();
        return DatabaseTemplateModel.builder()
                .tableList(tableList)
                .columnSize(columnSize)
                .currentDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE))
                .build();
    }
}
