package com.smart.framework.crud.mybatis.handler;

/**
 * 字符串数组转为逗号分隔字符串转换器
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-16 22:35
 * @since 5.0.0
 */
public class StringSplitTypeHandler extends AbstractSplitTypeHandler<String> {
    /**
     * 转换字符串为指定类型
     *
     * @param value 字符串值
     * @return 转换后的类型
     */
    @Override
    protected String convert(String value) {
        return value;
    }
}
