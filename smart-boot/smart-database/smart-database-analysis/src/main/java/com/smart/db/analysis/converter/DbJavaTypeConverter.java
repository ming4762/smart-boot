package com.smart.db.analysis.converter;

import com.smart.db.analysis.constants.TypeMappingEnum;
import com.smart.db.analysis.pojo.bo.ColumnBO;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * DB类型转java类型转换器
 * @author ShiZhongMing
 * 2020/8/3 10:58
 * @since 1.0
 */
public interface DbJavaTypeConverter {

    /**
     * 返回java类型
     * @param column 列信息
     * @return 类型映射
     */
    @Nullable
    TypeMappingEnum convert(@NonNull ColumnBO column);
}
