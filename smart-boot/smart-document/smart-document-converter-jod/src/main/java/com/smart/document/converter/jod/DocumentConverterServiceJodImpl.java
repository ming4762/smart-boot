package com.smart.document.converter.jod;

import com.smart.document.constants.DocumentFormatEnum;
import com.smart.document.service.DocumentConverterService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.job.ConversionJobWithOptionalSourceFormatUnspecified;
import org.jodconverter.core.job.ConversionJobWithSourceSpecified;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 文档转换器 job libreoffice实现
 * @author ShiZhongMing
 * 2021/8/27 16:51
 * @since 1.0
 */
@Slf4j
public class DocumentConverterServiceJodImpl implements DocumentConverterService {
    private final DocumentConverter documentConverter;

    public DocumentConverterServiceJodImpl(DocumentConverter documentConverter) {
        this.documentConverter = documentConverter;
    }

    @SneakyThrows
    @Override
    public void convert(InputStream inputStream, OutputStream outputStream, @Nullable DocumentFormatEnum fromFormat, @NonNull DocumentFormatEnum toFormat) {
        log.info("开始进行文档转换，转换源格式：{}，目标格式：{}", fromFormat == null ? "未知" : fromFormat.name(), toFormat.name());
        long current = System.nanoTime();
        ConversionJobWithSourceSpecified formatUnspecified = documentConverter.convert(inputStream, false);
        if (fromFormat != null) {
            formatUnspecified = ((ConversionJobWithOptionalSourceFormatUnspecified) formatUnspecified).as(Objects.requireNonNull(DefaultDocumentFormatRegistry.getFormatByExtension(fromFormat.name())));
        }
        formatUnspecified.to(outputStream, false)
                .as(Objects.requireNonNull(DefaultDocumentFormatRegistry.getFormatByExtension(toFormat.name())))
                .execute();
        log.info("文档转换结束，用时：{}ms", TimeUnit.MILLISECONDS.convert(System.nanoTime() - current, TimeUnit.NANOSECONDS));
    }
}
