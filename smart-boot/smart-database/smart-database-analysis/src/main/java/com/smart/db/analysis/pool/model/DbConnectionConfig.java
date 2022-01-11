package com.smart.db.analysis.pool.model;

import com.smart.db.analysis.constants.DatabaseTypeEnum;
import com.smart.db.analysis.constants.ExceptionConstant;
import com.smart.db.analysis.exception.SmartDatabaseException;
import lombok.*;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.Driver;

/**
 * 数据库连接配置
 * @author ShiZhongMing
 * 2021/2/10 16:25
 * @since 1.0
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DbConnectionConfig implements Serializable {
    private static final long serialVersionUID = -4218462921900208047L;

    private String databaseName;

    private DatabaseTypeEnum type;

    private String url;

    private String username;

    private String password;

    private String tableSchema;

    public Driver doGetDriver() {
        final String driverClass = this.doGetDriverClass();
        try {
            final Class<?> clazz = Class.forName(driverClass);
            return (Driver) clazz.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            throw new SmartDatabaseException(ExceptionConstant.DIRVER_CLASS_NOT_FOUND, driverClass);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new SmartDatabaseException(ExceptionConstant.DIRVER_CLASS_INSTANCE, driverClass);
        }
    }

    public String doGetDriverClass() {
        return this.getType().getDriverClass();
    }

}
