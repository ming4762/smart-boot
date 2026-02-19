package com.smart.framework.crud.mybatis.handler;

/**
 * long数组转为逗号分隔字符串转换器
 * @author zhongming4762
 * 2023/7/13
 * @since 3.0.0
 */
public class LongSplitTypeHandler extends AbstractSplitTypeHandler<Long> {


    /**
     * 转换字符串为指定类型
     *
     * @param value 字符串值
     * @return 转换后的类型
     */
    @Override
    protected Long convert(String value) {
        return Long.valueOf(value);
    }
}
