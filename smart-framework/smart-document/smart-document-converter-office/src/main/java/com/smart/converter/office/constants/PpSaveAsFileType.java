package com.smart.converter.office.constants;

import com.smart.converter.office.converter.ConvertFileType;
import lombok.Getter;

/**
 * 指定要另存为的文件类型的常量，这些常量将传递给 Presentation 对象的 SaveAs 方法
 * @author ShiZhongMing
 * 2021/8/25 15:19
 * @since 1.0
 */
public enum PpSaveAsFileType implements ConvertFileType {
    /**
     * 指定要另存为的文件类型的常量，这些常量将传递给 Presentation 对象的 SaveAs 方法
     */
    PP_SAVE_AS_ADD_IN(8),
    PP_SAVE_AS_ANIMATED_GIF(40),
    PP_SAVE_AS_BMP(19),
    PP_SAVE_AS_DEFAULT(11),
    PP_SAVE_AS_EMF(23),
    PP_SAVE_AS_EXTERNAL_CONVERTER(64000),
    PP_SAVE_AS_GIF(16),
    PP_SAVE_AS_JPG(17),
    PP_SAVE_AS_META_FILE(15),
    PP_SAVE_AS_MP4(39),
    PP_SAVE_AS_OPEN_DOCUMENT_PRESENTATION(35),
    PP_SAVE_AS_OPEN_XML_ADDIN(30),
    PP_SAVE_AS_OPEN_XML_PICTURE_PRESENTATION(36),
    PP_SAVE_AS_OPEN_XML_PRESENTATION(24),
    PP_SAVE_AS_OPEN_XML_PRESENTATION_MACRO_ENABLED(25),
    PP_SAVE_AS_OPEN_XML_SHOW(28),
    PP_SAVE_AS_OPEN_XML_SHOW_MACRO_ENABLED(29),
    PP_SAVE_AS_OPEN_XML_TEMPLATE(26),
    PP_SAVE_AS_OPEN_XML_TEMPLATE_MACRO_ENABLED(27),
    PP_SAVE_AS_OPEN_XML_THEME(31),
    PP_SAVE_AS_PDF(32),
    PP_SAVE_AS_PNG(18),
    PP_SAVE_AS_PRESENTATION(1),
    PP_SAVE_AS_RTF(6),
    PP_SAVE_AS_SHOW(7),
    PP_SAVE_AS_STRICT_OPEN_XML_PRESENTATION(38),
    PP_SAVE_AS_TEMPLATE(5),
    PP_SAVE_AS_TIF(21),
    PP_SAVE_AS_WMV(37),
    PP_SAVE_AS_XML_PRESENTATION(34),
    PP_SAVE_AS_XPS(33)
    ;

    @Getter
    private final int value;

    PpSaveAsFileType(int value) {
        this.value = value;
    }
}
