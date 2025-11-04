package com.smart.framework.crud.plus.tenant;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.mapping.SqlCommandType;

import java.io.Closeable;
import java.util.List;

/**
 * 租户忽略数据配置
 * 支持closeable，在使用完后需要手动调用close方法释放资源
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/4 14:09
 * @since 5.0.0
 */
@Getter
@Setter
public class SmartTenantIgnoreData implements Closeable {

    private List<SqlCommandType> ignoreCommandList;
    private String tableName;
    private List<SqlCommandType> platformTenantIgnoreCommandList;

    protected SmartTenantIgnoreData(String tableName, List<SqlCommandType> ignoreCommandList, List<SqlCommandType> platformTenantIgnoreCommandList) {
        this.tableName = tableName;
        this.ignoreCommandList = ignoreCommandList == null ? List.of() : ignoreCommandList;
        this.platformTenantIgnoreCommandList = platformTenantIgnoreCommandList == null? List.of() : platformTenantIgnoreCommandList;
    }


    /**
     * 清除租户忽略数据配置
     */
    @Override
    public void close() {
        SmartTenantControl.restIgnore(this.tableName);
    }
}
