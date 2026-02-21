package com.smart.framework.crud.mybatis.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 将字符串按照指定分隔符拆分为List
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-16 22:31
 * @since 5.0.0
 */
public abstract class AbstractSplitTypeHandler<T> implements TypeHandler<List<T>> {

    @Override
    public void setParameter(PreparedStatement ps, int i, List<T> parameter, JdbcType jdbcType) throws SQLException {
        if (!CollectionUtils.isEmpty(parameter)) {
            ps.setString(i, parameter.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(this.splitStr())));
        } else {
            ps.setString(i, null);
        }
    }

    @Override
    public List<T> getResult(ResultSet rs, String columnName) throws SQLException {
        String result = rs.getString(columnName);
        return this.getResult(result);
    }

    @Override
    public List<T> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return this.getResult(rs.getString(columnIndex));
    }

    @Override
    public List<T> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return this.getResult(cs.getString(columnIndex));
    }

    protected List<T> getResult(String result) {
        if (!StringUtils.hasText(result)) {
            return new ArrayList<>(0);
        }
        return Arrays.stream(result.split(this.splitStr()))
                .map(this::convert)
                .toList();
    }

    protected String splitStr() {
        return ",";
    }

    /**
     * 转换字符串为指定类型
     * @param value 字符串值
     * @return 转换后的类型
     */
    protected abstract T convert(String value);
}
