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
     * word保存格式
     */
    wdFormatDocument(0, ""),
    wdFormatDOSText(4, ""),
    wdFormatDOSTextLineBreaks(5, ""),
    wdFormatEncodedText(7, ""),
    wdFormatFilteredHTML(10, ""),
    wdFormatFlatXML(19, ""),
    wdFormatFlatXMLMacroEnabled(20, ""),
    wdFormatFlatXMLTemplate(21, ""),
    wdFormatFlatXMLTemplateMacroEnabled(22, ""),
    wdFormatOpenDocumentText(23, ""),
    wdFormatHTML(8, ""),
    wdFormatRTF(6, ""),
    wdFormatStrictOpenXMLDocument(24, ""),
    wdFormatTemplate(1, ""),
    wdFormatText(2, ""),
    wdFormatTextLineBreaks(3, ""),
    wdFormatUnicodeText(7, ""),
    wdFormatWebArchive(9, ""),
    wdFormatXML(11, ""),
    wdFormatDocument97(0, ""),
    wdFormatDocumentDefault(16, ""),
    wdFormatPDF(17, ""),
    wdFormatTemplate97(1, ""),
    wdFormatXMLDocument(12, ""),
    wdFormatXMLDocumentMacroEnabled(13, ""),
    wdFormatXMLTemplate(14, ""),
    wdFormatXMLTemplateMacroEnabled(15, ""),
    wdFormatXPS(18, "")
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
