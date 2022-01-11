package com.smart.system.mybatis.type;

import com.smart.starter.log.constants.LogSourceEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 日志来源枚举 mybatis 类型处理器
 * @author ShiZhongMing
 * 2021/12/30
 * @since 1.0
 */
public class LogSourceTypeHandler implements TypeHandler<LogSourceEnum> {

    private static final Map<String, LogSourceEnum> LOG_SOURCE_MAP = Arrays.stream(LogSourceEnum.values())
            .collect(Collectors.toMap(LogSourceEnum :: getValue, item -> item));

    @Override
    public void setParameter(PreparedStatement ps, int i, LogSourceEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, Optional.ofNullable(parameter).map(LogSourceEnum::getValue).orElse(null));
    }

    @Override
    public LogSourceEnum getResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (StringUtils.isNotBlank(value)) {
            return LOG_SOURCE_MAP.get(value);
        }
        return null;
    }

    @Override
    public LogSourceEnum getResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (StringUtils.isNotBlank(value)) {
            return LOG_SOURCE_MAP.get(value);
        }
        return null;
    }

    @Override
    public LogSourceEnum getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (StringUtils.isNotBlank(value)) {
            return LOG_SOURCE_MAP.get(value);
        }
        return null;
    }
}
