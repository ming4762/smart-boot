package com.smart.db.generator.service;

import com.smart.db.analysis.pool.model.DbConnectionConfig;
import com.smart.db.generator.engine.data.TemplateElement;
import org.springframework.lang.NonNull;

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
    void createDatabaseDic(@NonNull DbConnectionConfig config, @NonNull OutputStream outputStream, @NonNull TemplateElement templateElement);

    /**
     * 创建数据库字段
     * @param config 数据库连接信息
     * @param outputStream 输出流
     * @param templateName 模板名字
     * @param templateValue 模板内容
     */
    void createDatabaseDic(@NonNull DbConnectionConfig config, @NonNull OutputStream outputStream, @NonNull String templateName, @NonNull String templateValue);
}
