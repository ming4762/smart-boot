package com.smart.system.mybatis.type;

import com.smart.system.constants.UserAccountStatusEnum;
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
public class UserAccountStatusTypeHandler extends BaseTypeHandler<UserAccountStatusEnum> {

    private static final Map<String, UserAccountStatusEnum> DATA_MAP = Arrays.stream(UserAccountStatusEnum.values())
            .collect(Collectors.toMap(UserAccountStatusEnum::getValue, item -> item));

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, UserAccountStatusEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    @Override
    public UserAccountStatusEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Optional.ofNullable(rs.getString(columnName))
                .map(DATA_MAP::get)
                .orElse(null);
    }

    @Override
    public UserAccountStatusEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Optional.ofNullable(rs.getString(columnIndex))
                .map(DATA_MAP::get)
                .orElse(null);
    }

    @Override
    public UserAccountStatusEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Optional.ofNullable(cs.getString(columnIndex))
                .map(DATA_MAP::get)
                .orElse(null);
    }
}
