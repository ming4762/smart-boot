package com.smart.framework.freemarker.engine;

import com.smart.framework.freemarker.template.SmartTemplateElement;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

/**
 * 模板引擎接口
 * TODO：此接口应为公共接口，应该放到common模块中，而freemarker作为一种实现
 * @author shizhongming
 * 2024/11/11 10:08
 * @since 5.0.0
 */
public interface TemplateEngine {

    /**
     * 执行引擎并将结果写入到输出流
     * @param templateElement 模板信息
     * @param model 模板数据
     * @param outputStream 输出流
     */
    void processToOutputStream(@NonNull SmartTemplateElement templateElement, @NonNull Object model, @NonNull OutputStream outputStream);

    /**
     * 执行引擎并将结果写入到writer中
     * @param templateElement 模板信息
     * @param model 模板数据
     * @param out 输出流
     */
    void processToWriter(@NonNull SmartTemplateElement templateElement, @NonNull Object model, @NonNull Writer out);

    /**
     * 执行引擎并返回结果
     * @param templateElement 模板信息
     * @param model 模板数据
     * @return 模板结果
     */
    @SneakyThrows(IOException.class)
    default String processToString(@NonNull SmartTemplateElement templateElement, @NonNull Object model) {
        try (StringWriter stringWriter = new StringWriter()) {
            this.processToWriter(templateElement, model, stringWriter);
            return stringWriter.toString();
        }
    }
}
