package com.smart.module.document.controller;

import com.smart.document.model.code.BarcodeGeneratorData;
import com.smart.document.model.code.QrcodeGeneratorData;
import com.smart.document.service.CodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 二维码条形码工具
 * @author ShiZhongMing
 * 2021/8/13 20:09
 * @since 1.0
 */
@Controller
@RequestMapping("document/code")
@Tag(name = "二维码条形码生成接口", description = "二维码条形码生成接口")
public class CodeController {

    private final CodeService codeService;

    public CodeController(CodeService codeService) {
        this.codeService = codeService;
    }

    /**
     * 生成条形码
     */
    @SneakyThrows
    @PostMapping("generateBarcode")
    @Operation(summary = "生成条形码")
    public void generateBarcode(@RequestBody @Valid BarcodeGeneratorData parameter, HttpServletResponse response) {
        response.setContentType("image/png");
        this.codeService.generateBarcode(parameter, response.getOutputStream());
    }

    /**
     * 生成二维码
     * @param parameter 参数
     * @param response HttpServletResponse
     */
    @SneakyThrows
    @PostMapping("generateQrcode")
    @Operation(summary = "生成二维码")
    public void generateQrcode(@RequestBody @Valid QrcodeGeneratorData parameter, HttpServletResponse response) {
        response.setContentType("image/png");
        this.codeService.generateQrcode(parameter, response.getOutputStream());
    }
}
