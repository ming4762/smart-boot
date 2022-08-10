package com.smart.module.document.controller;

import com.smart.commons.core.i18n.I18nException;
import com.smart.document.constants.DocumentFormatEnum;
import com.smart.document.service.DocumentConverterService;
import com.smart.document.service.ExcelService;
import com.smart.module.document.i18n.DocumentI18nMessage;
import com.smart.module.document.model.DocumentTemplatePO;
import com.smart.module.document.pojo.dto.converter.DocumentConvertDTO;
import com.smart.module.document.pojo.dto.converter.ExcelFillConvertDataDTO;
import com.smart.module.document.pojo.dto.converter.ExcelMultiSheetConvertDTO;
import com.smart.module.document.pojo.dto.converter.ExcelSingleSheetConvertDTO;
import com.smart.module.document.service.DocumentExcelService;
import com.smart.module.document.service.DocumentTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author ShiZhongMing
 * 2021/8/13 20:08
 * @since 1.0
 */
@Controller
@RequestMapping("document/convert")
@Tag(name = "文档转换接口接口")
public class DocumentConverterController {

    private final DocumentTemplateService documentTemplateService;

    private final DocumentConverterService documentConverterService;

    private final ExcelService excelService;

    private final DocumentExcelService documentExcelService;


    public DocumentConverterController(DocumentTemplateService documentTemplateService, DocumentConverterService documentConverterService, ExcelService excelService, DocumentExcelService documentExcelService) {
        this.documentTemplateService = documentTemplateService;
        this.documentConverterService = documentConverterService;
        this.excelService = excelService;
        this.documentExcelService = documentExcelService;
    }

    /**
     * 填充转换单sheet（带有条形码/二维码）
     * @param parameter 参数
     * @param response 输出流
     * @throws IOException IOException
     */
    @Operation(summary = "填充转换单sheet Excel（带有条形码/二维码）")
    @PostMapping("fillConvertSingleSheetWithCode")
    public void fillConvertSingleSheetWithCode(@RequestBody @Valid ExcelSingleSheetConvertDTO parameter, HttpServletResponse response) throws IOException {
        ByteArrayInputStream inputStream = null;
        try (
                InputStream templateStream = this.getTemplateInputStream(parameter.getTemplateCode());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
                ) {
            // 填充excel
            this.documentExcelService.fillSingleWithCode(templateStream, outputStream, parameter.getData());
            inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            // 将excel转为PDF
            this.documentConverterService.convert(inputStream, response.getOutputStream(), DocumentFormatEnum.xlsx, DocumentFormatEnum.pdf);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * 填充转换多sheet Excel（带有条形码/二维码）
     * @param parameter 参数
     * @param response 输出流`````````````````
     * @throws IOException IOException
     */
    @Operation(summary = "填充转换多sheet Excel（带有条形码/二维码）")
    @PostMapping("fillConvertMultiSheetWithCode")
    public void fillConvertMultiSheetWithCode(@RequestBody @Valid ExcelMultiSheetConvertDTO parameter, HttpServletResponse response) throws IOException {
        ByteArrayInputStream inputStream = null;
        try (
                InputStream templateStream = this.getTemplateInputStream(parameter.getTemplateCode());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ) {
            // 填充excel
            this.documentExcelService.fillMultiWithCode(templateStream, outputStream, parameter.getDataList(), parameter.getSheetNameList());
            inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            // 将excel转为PDF
            this.documentConverterService.convert(inputStream, response.getOutputStream(), DocumentFormatEnum.xlsx, DocumentFormatEnum.pdf);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * 填充转换excel
     * @param parameter 参数
     * @param response 输出流
     * @throws IOException IOException
     */
    @Operation(summary = "填充转换excel")
    @PostMapping("fillConvertExcel")
    public void fillConvertExcel(@RequestBody @Valid ExcelFillConvertDataDTO parameter, HttpServletResponse response) throws IOException {
        ByteArrayInputStream inputStream = null;
        try (
                InputStream templateStream = this.getTemplateInputStream(parameter.getTemplateCode());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ) {
            // 填充excel
            this.excelService.fillExcel(templateStream, outputStream, parameter.getData());
            inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            // 将excel转为PDF
            this.documentConverterService.convert(inputStream, response.getOutputStream(), DocumentFormatEnum.xlsx, DocumentFormatEnum.pdf);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * 转换文档
     * @param parameter 参数
     * @param response 输出流
     */
    @SneakyThrows(IOException.class)
    @PostMapping("convert")
    @Operation(summary = "转换文档")
    public void convert(@Valid DocumentConvertDTO parameter, HttpServletResponse response) {
        if (parameter.getToFormat().equals(DocumentFormatEnum.pdf)) {
            response.setContentType("application/pdf");
        }
        this.documentConverterService.convert(parameter.getFile().getInputStream(), response.getOutputStream(), parameter.getFromFormat(), parameter.getToFormat());
    }


    /**
     * 获取模板输入流
     * @param templateCode 模板编码
     * @return 模板InputStream
     */
    private InputStream getTemplateInputStream(String templateCode) {
        DocumentTemplatePO template = this.documentTemplateService.getByCode(templateCode);
        if (template == null) {
            throw new I18nException(DocumentI18nMessage.TEMPLATE_CODE_NOT_FOUND);
        }
        return new ByteArrayInputStream(template.getData());
    }

}
