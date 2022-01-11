package com.smart.crud.mybatis.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

/**
 * @author ShiZhongMing
 * 2021/11/16
 * @since 1.0.7
 */
public class LocaleTypeHandler implements TypeHandler<Locale> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Locale parameter, JdbcType jdbcType) throws SQLException {
        if (parameter != null) {
            ps.setString(i, parameter.toLanguageTag());
        }
    }

    @Override
    public Locale getResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return Locale.forLanguageTag(value);
    }

    @Override
    public Locale getResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return Locale.forLanguageTag(value);
    }

    @Override
    public Locale getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return Locale.forLanguageTag(value);
    }
}
