package com.smart.crud.mybatis.handler;

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
 * long数组转为逗号分隔字符串转换器
 * @author zhongming4762
 * 2023/7/13
 * @since 3.0.0
 */
public class LongSplitTypeHandler implements TypeHandler<List<Long>> {

    private static final String SPLIT_STR = ",";

    @Override
    public void setParameter(PreparedStatement ps, int i, List<Long> parameter, JdbcType jdbcType) throws SQLException {
        if (!CollectionUtils.isEmpty(parameter)) {
            ps.setString(i, parameter.stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(SPLIT_STR)));
        } else {
            ps.setString(i, null);
        }
    }

    /**
     * Gets the result.
     *
     * @param rs         the rs
     * @param columnName Column name, when configuration <code>useColumnLabel</code> is <code>false</code>
     * @return the result
     * @throws SQLException the SQL exception
     */
    @Override
    public List<Long> getResult(ResultSet rs, String columnName) throws SQLException {
        String result = rs.getString(columnName);
        return this.getResult(result);
    }

    @Override
    public List<Long> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return this.getResult(rs.getString(columnIndex));
    }

    @Override
    public List<Long> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return this.getResult(cs.getString(columnIndex));
    }

    private List<Long> getResult(String result) {
        if (!StringUtils.hasText(result)) {
            return new ArrayList<>(0);
        }
        return Arrays.stream(result.split(SPLIT_STR))
                .map(Long::valueOf)
                .toList();
    }
}
