package com.smart.module.document.controller;

import com.smart.commons.core.i18n.I18nException;
import com.smart.document.service.ExcelService;
import com.smart.module.document.i18n.DocumentI18nMessage;
import com.smart.module.document.model.DocumentTemplatePO;
import com.smart.module.document.pojo.dto.excel.ExcelFillDataDTO;
import com.smart.module.document.pojo.dto.excel.ExcelMultiSheetDTO;
import com.smart.module.document.pojo.dto.excel.ExcelSingleSheetDTO;
import com.smart.module.document.service.DocumentExcelService;
import com.smart.module.document.service.DocumentTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author ShiZhongMing
 * 2021/8/13 20:08
 * @since 1.0
 */
@Controller
@RequestMapping("document/excel")
@Tag(name = "Excel处理接口", description = "Excel处理接口")
public class ExcelController {

    private final ExcelService excelService;

    private final DocumentExcelService documentExcelService;

    private final DocumentTemplateService documentTemplateService;

    public ExcelController(DocumentTemplateService documentTemplateService, ExcelService excelService, DocumentExcelService documentExcelService) {
        this.documentTemplateService = documentTemplateService;
        this.excelService = excelService;
        this.documentExcelService = documentExcelService;
    }

    /**
     * 填充单一sheet excel
     * @param parameter 参数
     * @param response HttpServletResponse
     */
    @SneakyThrows
    @PostMapping("fillSingleWithCode")
    @Operation(summary = "填充单一sheetExcel(带有条形码/二维码)")
    public void fillSingleWithCode(@RequestBody @Valid ExcelSingleSheetDTO parameter, HttpServletResponse response) {
        try (InputStream inputStream = this.getTemplateInputStream(parameter.getTemplateCode())) {
            this.documentExcelService.fillSingleWithCode(inputStream, response.getOutputStream(), parameter.getData());
        }
    }

    /**
     * 填充多sheet excel
     * @param parameter 参数
     * @param response HttpServletResponse
     */
    @SneakyThrows
    @PostMapping("fillMultiWithCode")
    @Operation(summary = "填充多sheetExcel(带有条形码/二维码)")
    public void fillMultiWithCode(@RequestBody @Valid ExcelMultiSheetDTO parameter, HttpServletResponse response) {
        try (
                InputStream inputStream = this.getTemplateInputStream(parameter.getTemplateCode())
        ) {
            this.documentExcelService.fillMultiWithCode(inputStream, response.getOutputStream(), parameter.getDataList(), parameter.getSheetNameList());
        }
    }

    /**
     * 填充excel
     * @param parameter 参数
     * @param response HttpServletResponse
     * @throws IOException IOException
     */
    @PostMapping("fillExcel")
    @Operation(summary = "填充Excel")
    public void fillExcel(@RequestBody @Valid ExcelFillDataDTO parameter, HttpServletResponse response) throws IOException {
        try (
                InputStream inputStream =  this.getTemplateInputStream(parameter.getTemplateCode())
                ) {
            this.excelService.fillExcel(inputStream, response.getOutputStream(), parameter.getData());
        }
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

