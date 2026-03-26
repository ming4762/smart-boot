package com.smart.converter.office.constants;

import com.smart.converter.office.converter.ConvertFileType;
import lombok.Getter;

/**
 * @author ShiZhongMing
 * 2021/8/25 16:34
 * @since 1.0
 */
public enum XlFileFormat implements ConvertFileType {
    // 保存格式
    XL_ADD_IN(18, "", ""),
    XL_ADD_IN_8(18, "", ""),
    XL_CSV(6, "", ""),
    XL_CSV_MAC(22, "", ""),
    XL_CSV_MSDOS(24, "", ""),
    XL_CSV_UTF8(62, "", ""),
    XL_CSV_WINDOWS(23, "", ""),
    XL_CURRENT_PLATFORM_TEXT(-4158, "", ""),
    XL_DBF2(7, "", ""),
    XL_DBF3(8, "", ""),
    XL_DBF4(11, "", ""),
    XL_DIF(9, "", ""),
    XL_EXCEL_12(50, "", ""),
    XL_EXCEL_2(16, "", ""),
    XL_EXCEL_2_FAR_EAST(27, "", ""),
    XL_EXCEL_3(29, "", ""),
    XL_EXCEL_4(33, "", ""),
    XL_EXCEL_4_WORKBOOK(35, "", ""),
    XL_EXCEL_5(39, "", ""),
    XL_EXCEL_7(39, "", ""),
    XL_EXCEL_8(56, "", ""),
    XL_EXCEL_9795(43, "", ""),
    XL_HTML(44, "", ""),
    XL_INTL_ADD_IN(26, "", ""),
    XL_INTL_MACRO(25, "", ""),
    XL_OPEN_DOCUMENT_SPREADSHEET(60, "", ""),
    XL_OPEN_XML_ADD_IN(55, "", ""),
    XL_OPEN_XML_STRICT_WORKBOOK(61, "", ""),
    XL_OPEN_XML_TEMPLATE(54, "", ""),
    XL_OPEN_XML_TEMPLATE_MACRO_ENABLED(53, "", ""),
    XL_OPEN_XML_WORKBOOK(51, "", ""),
    XL_OPEN_XML_WORKBOOK_MACRO_ENABLED(52, "", ""),
    XL_SYLK(2, "", ""),
    XL_TEMPLATE(17, "", ""),
    XL_TEMPLATE_8(17, "", ""),
    XL_TEXT_MAC(19, "", ""),
    XL_TEXT_MSDOS(21, "", ""),
    XL_TEXT_PRINTER(36, "", ""),
    XL_TEXT_WINDOWS(20, "", ""),
    XL_UNICODE_TEXT(42, "", ""),
    XL_WEB_ARCHIVE(45, "", ""),
    XL_WJ2_WD1(14, "", ""),
    XL_WJ3(40, "", ""),
    XL_WJ3_FJ3(41, "", ""),
    XL_WK1(5, "", ""),
    XL_WK1_ALL(31, "", ""),
    XL_WK1_FMT(30, "", ""),
    XL_WK3(15, "", ""),
    XL_WK3_FM3(32, "", ""),
    XL_WK4(38, "", ""),
    XL_WKS(4, "", ""),
    XL_WORKBOOK_DEFAULT(51, "", ""),
    XL_WORKBOOK_NORMAL(-4143, "", ""),
    XL_WORKS_2_FAR_EAST(28, "", ""),
    XL_WQ1(34, "", ""),
    XL_XML_SPREADSHEET(46, "", ""),

    // 转换格式
    XL_TYPE_PDF(0, "PDF- 可移植文档格式文件 (.pdf)", "pdf"),
    XL_TYPE_XPS(1, "XPS- XPS 文档 (.xps)", "xps")
    ;

    private final int value;

    @Getter
    private final String desc;

    private final String extension;

    XlFileFormat(int value, String desc, String extension) {
        this.value = value;
        this.desc = desc;
        this.extension = extension;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public String getExtension() {
        return this.extension;
    }


}
