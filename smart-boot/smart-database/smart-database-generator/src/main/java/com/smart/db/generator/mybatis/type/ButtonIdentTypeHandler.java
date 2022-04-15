package com.smart.db.generator.mybatis.type;

import com.smart.db.generator.constants.ButtonIdentEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2022/4/12
 * @since 2.0.0
 */
public class ButtonIdentTypeHandler extends BaseTypeHandler<ButtonIdentEnum> {

    private static final Map<String, ButtonIdentEnum> DATA_MAP = Arrays.stream(ButtonIdentEnum.values())
            .collect(Collectors.toMap(ButtonIdentEnum::getValue, item -> item));

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ButtonIdentEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    @Override
    public ButtonIdentEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Optional.ofNullable(rs.getString(columnName))
                .map(DATA_MAP::get)
                .orElse(null);
    }

    @Override
    public ButtonIdentEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Optional.ofNullable(rs.getString(columnIndex))
                .map(DATA_MAP::get)
                .orElse(null);
    }

    @Override
    public ButtonIdentEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Optional.ofNullable(cs.getString(columnIndex))
                .map(DATA_MAP::get)
                .orElse(null);
    }
}
