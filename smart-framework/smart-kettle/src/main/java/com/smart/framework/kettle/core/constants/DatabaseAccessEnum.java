package com.smart.framework.kettle.core.constants;

import com.smart.framework.commons.core.constants.LabelValueEnum;
import lombok.Getter;

/**
 * KETTLE 数据库连接方式
 * @author shizhongming
 * 2024/3/18 21:47
 * @since 3.0.0
 */
@Getter
public enum DatabaseAccessEnum implements LabelValueEnum {
    /**
     * 数据库连接方式
     */
    JDBC("Native(JDBC)"),
    ODBC("ODBC"),
    JNDI("JNDI"),
    OCI("OCI")
    ;

    private final String label;

    DatabaseAccessEnum(String label) {
        this.label = label;
    }

    /**
     * 获取value
     *
     * @return value
     */
    @Override
    public String getValue() {
        return this.name();
    }
}
