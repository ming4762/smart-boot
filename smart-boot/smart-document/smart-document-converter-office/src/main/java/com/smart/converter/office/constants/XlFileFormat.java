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
    xlAddIn(18, "", ""),
    xlAddIn8(18, "", ""),
    xlCSV(6, "", ""),
    xlCSVMac(22, "", ""),
    xlCSVMSDOS(24, "", ""),
    xlCSVUTF8(62, "", ""),
    xlCSVWindows(23, "", ""),
    xlCurrentPlatformText(-4158, "", ""),
    xlDBF2(7, "", ""),
    xlDBF3(8, "", ""),
    xlDBF4(11, "", ""),
    xlDIF(9, "", ""),
    xlExcel12(50, "", ""),
    xlExcel2(16, "", ""),
    xlExcel2FarEast(27, "", ""),
    xlExcel3(29, "", ""),
    xlExcel4(33, "", ""),
    xlExcel4Workbook(35, "", ""),
    xlExcel5(39, "", ""),
    xlExcel7(39, "", ""),
    xlExcel8(56, "", ""),
    xlExcel9795(43, "", ""),
    xlHtml(44, "", ""),
    xlIntlAddIn(26, "", ""),
    xlIntlMacro(25, "", ""),
    xlOpenDocumentSpreadsheet(60, "", ""),
    xlOpenXMLAddIn(55, "", ""),
    xlOpenXMLStrictWorkbook(61, "", ""),
    xlOpenXMLTemplate(54, "", ""),
    xlOpenXMLTemplateMacroEnabled(53, "", ""),
    xlOpenXMLWorkbook(51, "", ""),
    xlOpenXMLWorkbookMacroEnabled(52, "", ""),
    xlSYLK(2, "", ""),
    xlTemplate(17, "", ""),
    xlTemplate8(17, "", ""),
    xlTextMac(19, "", ""),
    xlTextMSDOS(21, "", ""),
    xlTextPrinter(36, "", ""),
    xlTextWindows(20, "", ""),
    xlUnicodeText(42, "", ""),
    xlWebArchive(45, "", ""),
    xlWJ2WD1(14, "", ""),
    xlWJ3(40, "", ""),
    xlWJ3FJ3(41, "", ""),
    xlWK1(5, "", ""),
    xlWK1ALL(31, "", ""),
    xlWK1FMT(30, "", ""),
    xlWK3(15, "", ""),
    xlWK3FM3(32, "", ""),
    xlWK4(38, "", ""),
    xlWKS(4, "", ""),
    xlWorkbookDefault(51, "", ""),
    xlWorkbookNormal(-4143, "", ""),
    xlWorks2FarEast(28, "", ""),
    xlWQ1(34, "", ""),
    xlXMLSpreadsheet(46, "", ""),

    // 转换格式
    xlTypePDF(0, "“PDF”- 可移植文档格式文件 (.pdf)", "pdf"),
    xlTypeXPS(1, "“XPS”- XPS 文档 (.xps)", "xps")
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
