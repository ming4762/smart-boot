package com.smart.db.generator.engine;

import com.smart.db.generator.engine.data.TemplateElement;
import freemarker.cache.ByteArrayTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.lang.NonNull;

import java.io.*;
import java.util.Objects;

/**
 * @author shizhongming
 * 2020/7/3 9:14 上午
 */
public class FreemarkerTemplateEngine implements TemplateEngine {

    private final ByteArrayTemplateLoader byteArrayTemplateLoader;

    private final Configuration configuration;

    public FreemarkerTemplateEngine(ByteArrayTemplateLoader byteArrayTemplateLoader, Configuration configuration) {
        this.byteArrayTemplateLoader = byteArrayTemplateLoader;
        this.configuration = configuration;
    }

    /**
     * 执行引擎并将接口写入到输出流
     * @param model 模板数据
     * @param outputStream 输出流
     * @param templateElement 模板信息
     */
    @SneakyThrows({IOException.class, TemplateException.class})
    @Override
    public void processToOutputStream(@NonNull Object model, @NonNull OutputStream outputStream, @NonNull TemplateElement templateElement) {
        final InputStream inputStream = templateElement.getInputStream();
        if (Objects.nonNull(inputStream)) {
            // 将输入流添加到模板加载器
            this.byteArrayTemplateLoader.putTemplate(templateElement.getTemplateName(), IOUtils.toByteArray(inputStream));
        }
        // 获取模板
        final Template template = this.configuration.getTemplate(templateElement.getTemplateName(), this.configuration.getDefaultEncoding());
        // 创建输出流并写入模板
        try (final Writer out = new BufferedWriter(new OutputStreamWriter(outputStream, configuration.getDefaultEncoding()))) {
            template.process(model, out);
        }
    }

    /**
     * 执行模板
     * @param model 数据
     * @param templateName 模板名称
     * @param templateValue 模板内容
     * @param out 输出
     */
    @SneakyThrows({IOException.class, TemplateException.class})
    @Override
    public void processTemplate(@NonNull Object model, @NonNull String templateName, @NonNull String templateValue, @NonNull Writer out) {
        final Template template = new Template(templateName, templateValue, this.configuration);
        template.process(model, out);
    }
}
