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
    ppSaveAsAddIn(8),
    ppSaveAsAnimatedGIF(40),
    ppSaveAsBMP(19),
    ppSaveAsDefault(11),
    ppSaveAsEMF(23),
    ppSaveAsExternalConverter(64000),
    ppSaveAsGIF(16),
    ppSaveAsJPG(17),
    ppSaveAsMetaFile(15),
    ppSaveAsMP4(39),
    ppSaveAsOpenDocumentPresentation(35),
    ppSaveAsOpenXMLAddin(30),
    ppSaveAsOpenXMLPicturePresentation(36),
    ppSaveAsOpenXMLPresentation(24),
    ppSaveAsOpenXMLPresentationMacroEnabled(25),
    ppSaveAsOpenXMLShow(28),
    ppSaveAsOpenXMLShowMacroEnabled(29),
    ppSaveAsOpenXMLTemplate(26),
    ppSaveAsOpenXMLTemplateMacroEnabled(27),
    ppSaveAsOpenXMLTheme(31),
    ppSaveAsPDF(32),
    ppSaveAsPNG(18),
    ppSaveAsPresentation(1),
    ppSaveAsRTF(6),
    ppSaveAsShow(7),
    ppSaveAsStrictOpenXMLPresentation(38),
    ppSaveAsTemplate(5),
    ppSaveAsTIF(21),
    ppSaveAsWMV(37),
    ppSaveAsXMLPresentation(34),
    ppSaveAsXPS(33)
    ;

    @Getter
    private final int value;

    PpSaveAsFileType(int value) {
        this.value = value;
    }
}
