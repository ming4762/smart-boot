package com.smart.document.excel.jxls;

import com.smart.document.excel.jxls.utils.JxlsUtils;
import com.smart.document.service.ExcelService;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author ShiZhongMing
 * 2021/8/25 14:05
 * @since 1.0
 */
public class JxlsExcelServiceImpl implements ExcelService {
    @Override
    public void fillExcel(@NonNull InputStream inputStream, @NonNull OutputStream outputStream, Object model) throws IOException {
        JxlsUtils.exportExcel(inputStream, outputStream, model, null, jexlBuilder -> jexlBuilder.silent(true));
    }
}
