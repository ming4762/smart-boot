package com.smart.converter.office.service;

import com.google.common.collect.ImmutableMap;
import com.smart.converter.office.constants.PpSaveAsFileType;
import com.smart.converter.office.constants.WdSaveFormat;
import com.smart.converter.office.constants.XlFileFormat;
import com.smart.converter.office.converter.*;
import com.smart.framework.document.constants.DocumentFormatEnum;
import com.smart.framework.document.exception.ConvertSourceFormatNotSupportException;
import com.smart.framework.document.exception.ConvertToFormatNotSupportException;
import com.smart.framework.document.service.DocumentConverterService;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author ShiZhongMing
 * 2021/8/25 14:47
 * @since 1.0
 */
public class OfficeDocumentConverterServiceImpl implements DocumentConverterService {

    private static final String TEMP_DIR = "converter";

    /**
     * 文档类型与转换器对象关系
     */
    private static final Map<DocumentFormatEnum, Class<? extends OfficeConverter>> FORMAT_ENUM_CLASS_MAP = ImmutableMap.<DocumentFormatEnum, Class<? extends OfficeConverter>>builder()
            .put(DocumentFormatEnum.DOC, WordConverter.class)
            .put(DocumentFormatEnum.DOCX, WordConverter.class)
            .put(DocumentFormatEnum.XLS, ExcelConverter.class)
            .put(DocumentFormatEnum.XLSX, ExcelConverter.class)
            .put(DocumentFormatEnum.PPT, PowerPointConverter.class)
            .put(DocumentFormatEnum.PPTX, PowerPointConverter.class)
            .build();

    private static final Map<DocumentFormatEnum, Map<Class<? extends OfficeConverter>, ConvertFileType>> FORMAT_ENUM_CONVERT_FILE_TYPE_MAP = ImmutableMap.<DocumentFormatEnum, Map<Class<? extends OfficeConverter>, ConvertFileType>>builder()
            .put(DocumentFormatEnum.PDF, Map.of(
                    WordConverter.class, WdSaveFormat.WD_FORMAT_PDF,
                    ExcelConverter.class, XlFileFormat.XL_TYPE_PDF,
                    PowerPointConverter.class, PpSaveAsFileType.PP_SAVE_AS_PDF
            ))
            .put(DocumentFormatEnum.HTML, Map.of(
                    WordConverter.class, WdSaveFormat.WD_FORMAT_HTML,
                    ExcelConverter.class, XlFileFormat.XL_HTML
            ))
            .build();

    @SneakyThrows({IOException.class, NoSuchMethodException.class, InstantiationException.class,
            IllegalAccessException.class, IllegalArgumentException.class, InvocationTargetException.class})
    @Override
    public void convert(InputStream inputStream, OutputStream outputStream, DocumentFormatEnum fromFormat, DocumentFormatEnum toFormat) {
        // 判断源格式是否支持
        Class<? extends OfficeConverter> converterClass = FORMAT_ENUM_CLASS_MAP.get(fromFormat);
        if (converterClass == null) {
            throw new ConvertSourceFormatNotSupportException(fromFormat);
        }
        // 判断转出格式是否支持
        ConvertFileType convertFileType = Optional.ofNullable(FORMAT_ENUM_CONVERT_FILE_TYPE_MAP.get(toFormat))
                .map(item -> item.get(converterClass))
                .orElse(null);
        if (convertFileType == null) {
            throw new ConvertToFormatNotSupportException(toFormat);
        }
        // 判断文件扩展名-写文件
        String fromFilePath = Files.createTempDirectory(TEMP_DIR) + File.separator + UUID.randomUUID() + "." + fromFormat.name();
        try (
                FileOutputStream sourceOutputStream = new FileOutputStream(fromFilePath)
                ) {
            IOUtils.copy(inputStream, sourceOutputStream);
        }
        // 获取文件转换器 实例化 执行转换
        // 使用try with resource自动关闭文档及应用
        try (OfficeConverter converter = converterClass.getConstructor(String.class).newInstance(fromFilePath)) {
            converter.toFormat(convertFileType)
                    .to(outputStream);
        }

    }
}
