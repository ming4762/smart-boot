package com.smart.framework.document.constants;

import lombok.Getter;

/**
 * 支持的转换格式类型
 * @author ShiZhongMing
 * 2021/8/3 19:42
 * @since 1.0
 */
@Getter
public enum DocumentFormatEnum {

    /**
     * 支持的转换格式类型
     */
    PDF("pdf"),
    SWF("swf"),
    HTML("html"),
    XHTML("xhtml"),
    ODT("odt"),
   OTT("ott"),
    FODT("fodt"),
    SXW("sxw"),
    DOC("doc"),
    DOCX("docx"),
    DOTX("dotx"),
    RTF("rtf"),
    WPD("wpd"),
    TXT("txt"),
    ODS("ods"),
    OTS("ots"),
    FODS("fods"),
    SXC("sxc"),
    XLS("xls"),
    XLSX("xlsx"),
    CSV("csv"),
    TSV("tsv"),
    ODP("odp"),
    OTP("otp"),
    FODP("fodp"),
    SXI("sxi"),
    PPT("ppt"),
    PPTX("pptx"),
    ODG("odg"),
    OTG("otg"),
    FODG("fodg"),
    SVG("svg"),
    VSD("vsd"),
    VSDX("vsdx"),
    PNG("png"),
    JPG("jpg"),
    TIF("tif"),
    GIF("gif"),
    BMP("bmp");

    private final String value;

    DocumentFormatEnum(String value) {
        this.value = value;
    }
}
