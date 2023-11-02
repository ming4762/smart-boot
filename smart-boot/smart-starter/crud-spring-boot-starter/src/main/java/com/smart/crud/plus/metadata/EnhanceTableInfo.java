package com.smart.crud.plus.metadata;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.session.Configuration;

/**
 * @author shizhongming
 * 2023/10/24 20:32
 * @since 3.0.0
 */
public class EnhanceTableInfo extends TableInfo {
    /**
     * @param configuration 配置对象
     * @param entityType    实体类型
     * @since 3.4.4
     */
    public EnhanceTableInfo(Configuration configuration, Class<?> entityType) {
        super(configuration, entityType);
    }
}
