package com.smart.document.excel.jxls.utils;

import com.smart.commons.core.exception.BaseException;
import com.smart.document.excel.jxls.function.JxlsBooleanFunction;
import com.smart.document.excel.jxls.function.JxlsFormatFunction;
import lombok.NonNull;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.springframework.lang.Nullable;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Jxls Excel工具类
 * @author ShiZhongMing
 * 2021/8/4 18:25
 * @since 1.0
 */
public class JxlsUtils {

    private static final String FORMAT_FUNCTION_NAME = "format";

    private static final String BOOLEAN_FUNCTION_NAME = "bool";

    private JxlsUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 导出excel
     * @param templateIn 模板输入流
     * @param outputStream 模板输出流
     * @param model model
     * @param functionMap 函数
     * @throws IOException IOException
     */
    public static void exportExcel(InputStream templateIn, OutputStream outputStream, @NonNull Object model, @Nullable Map<String, Object> functionMap, Consumer<JexlBuilder> jexlBuilderHandler) throws IOException {
        // 构建参数
        Context context = new Context();
        if (model instanceof Map) {
            ((Map<?, ?>) model).forEach((key, value) -> context.putVar(key.toString(), value));
        } else {
            new BeanMap(model).forEach((key, value) -> context.putVar(key.toString(), value));
        }
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer = jxlsHelper.createTransformer(templateIn, outputStream);
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig()
                .getExpressionEvaluator();
        JexlBuilder jexlBuilder = new JexlBuilder();
        Map<String, Object> allFunctionMap = new HashMap<>(2);
        allFunctionMap.put(FORMAT_FUNCTION_NAME, new JxlsFormatFunction());
        allFunctionMap.put(BOOLEAN_FUNCTION_NAME, new JxlsBooleanFunction());
        // 设置自定义函数
        if (MapUtils.isNotEmpty(functionMap)) {
            if (functionMap.containsKey(FORMAT_FUNCTION_NAME)) {
                throw new BaseException(String.format("%s为内置函数名，请更换函数名", FORMAT_FUNCTION_NAME));
            }
            if (functionMap.containsKey(BOOLEAN_FUNCTION_NAME)) {
                throw new BaseException(String.format("%s为内置函数名，请更换函数名", BOOLEAN_FUNCTION_NAME));
            }
            allFunctionMap.putAll(functionMap);
        }
        jexlBuilder.namespaces(allFunctionMap);
        if (jexlBuilderHandler != null) {
            jexlBuilderHandler.accept(jexlBuilder);
        }
        JexlEngine jexlEngine = jexlBuilder.create();
        evaluator.setJexlEngine(jexlEngine);
        jxlsHelper.processTemplate(context, transformer);
    }

    /**
     * 导出excel
     * @param templateFile 模板文件
     * @param outFile 输出文件
     * @param model 模板数据
     * @param functionMap 自定义功能
     * @throws IOException IOException
     */
    public static void exportExcel(File templateFile, File outFile, @NonNull Object model, @Nullable Map<String, Object> functionMap, Consumer<JexlBuilder> jexlBuilderHandler) throws IOException {
        try (
                InputStream inputStream = new FileInputStream(templateFile)
                ) {
            exportExcel(inputStream, new FileOutputStream(outFile), model, functionMap, jexlBuilderHandler);
        }
    }
}
