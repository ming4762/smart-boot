package com.smart.module.code.service;

import com.smart.framework.freemarker.template.AbstractSmartTemplateElement;
import com.smart.framework.tool.database.pool.model.DbConnectionConfig;
import org.jspecify.annotations.NonNull;

import java.io.OutputStream;

/**
 * @author shizhongming
 * 2020/7/3 9:32 上午
 */
public interface DbDictGeneratorService {

    /**
     * 创建数据库字典
     * @param config 数据库连接信息
     * @param outputStream 输出流
     */
    void createDatabaseDic(@NonNull DbConnectionConfig config, @NonNull OutputStream outputStream);


    /**
     * 创建数据库字典
     * @param config 数据库连接信息
     * @param outputStream 输出流
     * @param templateElement 模板信息
     */
    void createDatabaseDic(@NonNull DbConnectionConfig config, @NonNull OutputStream outputStream, @NonNull AbstractSmartTemplateElement templateElement);

    /**
     * 创建数据库字段
     * @param config 数据库连接信息
     * @param outputStream 输出流
     * @param templateName 模板名字
     * @param templateValue 模板内容
     */
    void createDatabaseDic(@NonNull DbConnectionConfig config, @NonNull OutputStream outputStream, @NonNull String templateName, @NonNull String templateValue);
}
