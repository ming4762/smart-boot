package com.smart.db.analysis.converter;

import com.smart.db.analysis.constants.TypeMappingEnum;
import com.smart.db.analysis.pojo.bo.ColumnBO;
import com.smart.db.analysis.utils.CacheUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * 默认的类型转换器
 * @author ShiZhongMing
 * 2020/8/3 11:00
 * @since 1.0
 */
public class DefaultDbJavaTypeConverter implements DbJavaTypeConverter {

    private static final int INTEGER_MAX_LENGTH = 32;

    private static final int DECIMAL_DIGITS_MIN = -127;

    /**
     * 返回java类型
     * @param column 列信息
     * @return 类型映射
     */
    @Nullable
    @Override
    public TypeMappingEnum convert(@NonNull ColumnBO column) {
        // NUMERIC类型处理
        if (Objects.equals(column.getDataType(), TypeMappingEnum.NUMERIC.getDataType())) {
            return this.convertNumberType(column);
        }
        return CacheUtils.getFieldMapping(column.getDataType());
    }

    /**
     * 转换数字类型
     * @param column 列信息
     * @return 类型映射
     */
    @NonNull
    private TypeMappingEnum convertNumberType(@NonNull ColumnBO column) {
        // 判断是否是小数类型
        if (column.getDecimalDigits() > 0) {
            return TypeMappingEnum.NUMERIC;
        }
        // 判断长度是否是Integer Long
        int columnSize = column.getColumnSize();
        if (columnSize <= INTEGER_MAX_LENGTH && column.getDecimalDigits() != DECIMAL_DIGITS_MIN) {
            return TypeMappingEnum.INTEGER;
        }
        return TypeMappingEnum.BIGINT;
    }
}
