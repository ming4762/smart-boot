package com.smart.db.analysis.converter;

/**
 * @author shizhongming
 * 2022/12/14
 */
public class StringConverter implements Converter<Object, String>{
    /**
     * 进行转换
     *
     * @param resource 源数据
     * @return 转换后的数据
     */
    @Override
    public String convert(Object resource) {
        if (resource == null) {
            return null;
        }
        return resource.toString();
    }
}
