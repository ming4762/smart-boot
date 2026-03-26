package com.smart.converter.office.constants;

import com.smart.converter.office.converter.ConvertFileType;
import lombok.Getter;

/**
 * word保存格式
 * @author ShiZhongMing
 * 2021/8/26
 * @since 1.0
 */
public enum WdSaveFormat implements ConvertFileType {
    /**
     * word 保存格式
     */
    WD_FORMAT_DOCUMENT(0, ""),
    WD_FORMAT_DOS_TEXT(4, ""),
    WD_FORMAT_DOS_TEXT_LINE_BREAKS(5, ""),
    WD_FORMAT_ENCODED_TEXT(7, ""),
    WD_FORMAT_FILTERED_HTML(10, ""),
    WD_FORMAT_FLAT_XML(19, ""),
    WD_FORMAT_FLAT_XML_MACRO_ENABLED(20, ""),
    WD_FORMAT_FLAT_XML_TEMPLATE(21, ""),
    WD_FORMAT_FLAT_XML_TEMPLATE_MACRO_ENABLED(22, ""),
    WD_FORMAT_OPEN_DOCUMENT_TEXT(23, ""),
    WD_FORMAT_HTML(8, ""),
    WD_FORMAT_RTF(6, ""),
    WD_FORMAT_STRICT_OPEN_XML_DOCUMENT(24, ""),
    WD_FORMAT_TEMPLATE(1, ""),
    WD_FORMAT_TEXT(2, ""),
    WD_FORMAT_TEXT_LINE_BREAKS(3, ""),
    WD_FORMAT_UNICODE_TEXT(7, ""),
    WD_FORMAT_WEB_ARCHIVE(9, ""),
    WD_FORMAT_XML(11, ""),
    WD_FORMAT_DOCUMENT_97(0, ""),
    WD_FORMAT_DOCUMENT_DEFAULT(16, ""),
    WD_FORMAT_PDF(17, ""),
    WD_FORMAT_TEMPLATE_97(1, ""),
    WD_FORMAT_XML_DOCUMENT(12, ""),
    WD_FORMAT_XML_DOCUMENT_MACRO_ENABLED(13, ""),
    WD_FORMAT_XML_TEMPLATE(14, ""),
    WD_FORMAT_XML_TEMPLATE_MACRO_ENABLED(15, ""),
    WD_FORMAT_XPS(18, "")
    ;

    private final int value;

    @Getter
    private final String desc;

    WdSaveFormat(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public int getValue() {
        return this.value;
    }
}
