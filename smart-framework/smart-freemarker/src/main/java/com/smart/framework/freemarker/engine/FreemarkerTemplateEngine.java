package com.smart.framework.freemarker.engine;

import com.smart.framework.freemarker.template.SmartClassPathTemplateElement;
import com.smart.framework.freemarker.template.SmartInputStreamTemplateElement;
import com.smart.framework.freemarker.template.AbstractSmartTemplateElement;
import com.smart.framework.freemarker.template.SmartValueTemplateElement;
import freemarker.cache.ByteArrayTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * 模板引擎 FREEMARKER实现
 * @author shizhongming
 * 2024/11/11 10:24
 * @since 5.0.0
 */
public class FreemarkerTemplateEngine implements TemplateEngine {

    private final ByteArrayTemplateLoader byteArrayTemplateLoader;
    private final Configuration configuration;

    public FreemarkerTemplateEngine(Configuration configuration, ByteArrayTemplateLoader byteArrayTemplateLoader) {
        this.configuration = configuration;
        this.byteArrayTemplateLoader = byteArrayTemplateLoader;
    }


    /**
     * 执行引擎并将结果写入到输出流
     *
     * @param templateElement 模板信息
     * @param model           模板数据
     * @param outputStream    输出流
     */
    @SneakyThrows(IOException.class)
    @Override
    public void processToOutputStream(@NonNull AbstractSmartTemplateElement templateElement, @NonNull Object model, @NonNull OutputStream outputStream) {
        // 创建输出流并写入模板
        try (final Writer out = new BufferedWriter(new OutputStreamWriter(outputStream, configuration.getDefaultEncoding()))) {
            this.processToWriter(templateElement, model, out);
        }
    }

    /**
     * 执行引擎并将结果写入到writer中
     *
     * @param templateElement 模板信息
     * @param model           模板数据
     * @param out             输出流
     */
    @Override
    @SneakyThrows({IOException.class, TemplateException.class})
    public void processToWriter(@NonNull AbstractSmartTemplateElement templateElement, @NonNull Object model, @NonNull Writer out) {
        Template template = this.getTemplate(templateElement);
        template.process(model, out);
    }


    /**
     * 获取模板
     * @param templateElement 模板信息
     * @return 模板
     */
    @SneakyThrows(IOException.class)
    protected Template getTemplate(@NonNull AbstractSmartTemplateElement templateElement) {
        switch (templateElement) {
            case SmartValueTemplateElement smartValueTemplateElement -> {
                return new Template(smartValueTemplateElement.getName(), smartValueTemplateElement.getTemplateValue(), configuration);
            }
            case SmartInputStreamTemplateElement smartInputStreamTemplateElement -> {
                this.byteArrayTemplateLoader.putTemplate(templateElement.getName(), IOUtils.toByteArray(smartInputStreamTemplateElement.getInputStream()));
                return this.configuration.getTemplate(templateElement.getName(), this.configuration.getDefaultEncoding());
            }
            case SmartClassPathTemplateElement smartClassPathTemplateElement -> {
                return this.configuration.getTemplate(templateElement.getName(), this.configuration.getDefaultEncoding());
            }
            default -> {
                return null;
            }
        }
    }
}
