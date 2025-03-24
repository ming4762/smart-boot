package com.smart.converter.office.converter;

/**
 * @author ShiZhongMing
 * 2021/8/25 15:40
 * @since 1.0
 */
public interface ConvertFileType {
    /**
     * 获取转换文件类型
     * @return 转换文件类型
     */
    int getValue();

    /**
     * 获取扩展名
     * @return 扩展名
     */
    default String getExtension() {
        return "";
    }
}
