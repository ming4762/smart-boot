package com.smart.db.generator.engine;

import com.smart.db.generator.engine.data.TemplateElement;
import org.springframework.lang.NonNull;

import java.io.OutputStream;
import java.io.Writer;

/**
 * 模板引擎接口
 * @author shizhongming
 * 2020/7/3 9:06 上午
 */
public interface TemplateEngine {

    /**
     * 执行引擎并将接口写入到输出流
     * @param model 模板数据
     * @param outputStream 输出流
     * @param templateElement 模板信息
     */
    void processToOutputStream(@NonNull Object model, @NonNull OutputStream outputStream, @NonNull TemplateElement templateElement);

    /**
     * 执行模板
     * @param model 数据
     * @param templateName 模板名称
     * @param templateValue 模板内容
     * @param out 输出
     */
    void processTemplate(@NonNull Object model, @NonNull String templateName, @NonNull String templateValue, @NonNull Writer out);
}
