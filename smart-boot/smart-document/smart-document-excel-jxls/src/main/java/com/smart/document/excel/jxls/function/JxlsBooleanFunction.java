package com.smart.document.excel.jxls.function;

import com.smart.document.excel.jxls.exception.ExcelJxlsException;

import java.util.Arrays;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2021/8/4 21:03
 * @since 1.0
 */
public class JxlsBooleanFunction {

    /**
     * if else  判断
     * @param value 要判断的值
     * @param ifValue ifValue
     * @param resultValue resultValue
     * @return result
     */
    public String ifElse(Object value, String ifValue, String resultValue) {
        if (value == null) {
            return  "";
        }
        List<String> ifValueList = Arrays.asList(ifValue.split(","));
        List<String> resultValueList = Arrays.asList(resultValue.split(","));
        if (ifValueList.size() != resultValueList.size()) {
            throw new ExcelJxlsException("长度不一致");
        }
        String valueStr = value.toString();
        for (int i=0; i<ifValueList.size(); i++) {
            if (valueStr.equals(ifValueList.get(i))) {
                return resultValueList.get(i);
            }
        }
        return  "";
    }
}
