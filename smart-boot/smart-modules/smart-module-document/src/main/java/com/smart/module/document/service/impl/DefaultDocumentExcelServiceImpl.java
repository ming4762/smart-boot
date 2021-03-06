package com.smart.module.document.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.smart.document.service.CodeService;
import com.smart.document.service.ExcelService;
import com.smart.module.document.pojo.dto.excel.ExcelFillWithCodeData;
import com.smart.module.document.service.DocumentExcelService;
import lombok.SneakyThrows;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2021/8/27 13:51
 * @since 1.0
 */
@Service
public class DefaultDocumentExcelServiceImpl implements DocumentExcelService {

    private final CodeService codeService;

    private final ExcelService excelService;

    public DefaultDocumentExcelServiceImpl(CodeService codeService, ExcelService excelService) {
        this.codeService = codeService;
        this.excelService = excelService;
    }

    @SneakyThrows
    @Override
    public void fillSingleWithCode(InputStream inputStream, OutputStream outputStream, ExcelFillWithCodeData data) {
        this.excelService.fillExcel(inputStream, outputStream, this.createTemplateData(data));
    }

    @SneakyThrows
    @Override
    public void fillMultiWithCode(InputStream inputStream, OutputStream outputStream, List<ExcelFillWithCodeData> dataList, List<String> sheetNameList) {
        List<Map<String, Object>> templateDataList = dataList.stream()
                .map(this :: createTemplateData)
                .collect(Collectors.toList());
        this.excelService.fillExcel(inputStream, outputStream, ImmutableMap.of("dataList", templateDataList, "sheetNameList", sheetNameList));
    }

    /**
     * 创建模板数据
     * @param data 参数
     * @return 模板数据
     */
    private Map<String, Object> createTemplateData(ExcelFillWithCodeData data) {
        Map<String, Object> templateData = Maps.newHashMap();
        templateData.put("data", data.getData());
        // 处理条形码
        if (MapUtils.isNotEmpty(data.getBarcodeMap())) {
            Map<String, byte[]> barcodeMap = new HashMap<>(data.getBarcodeMap().size());
            data.getBarcodeMap().forEach((key, value) -> {
                try (ByteArrayOutputStream barcodeStream = new ByteArrayOutputStream()) {
                    this.codeService.generateBarcode(value, barcodeStream);
                    barcodeMap.put(key, barcodeStream.toByteArray());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            templateData.put("barcodeMap", barcodeMap);
        }
        // 处理二维码
        if (MapUtils.isNotEmpty(data.getQrcodeMap())) {
            Map<String, byte[]> qrcodeMap = new HashMap<>(data.getQrcodeMap().size());
            data.getQrcodeMap().forEach((key, value) -> {
                try (ByteArrayOutputStream qrcodeStream = new ByteArrayOutputStream()) {
                    this.codeService.generateQrcode(value, qrcodeStream);
                    qrcodeMap.put(key, qrcodeStream.toByteArray());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            templateData.put("qrcodeMap", qrcodeMap);
        }
        return templateData;
    }
}
