package com.smart.system.mybatis.type;

import com.smart.system.constants.UserTypeEnum;
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
 * 2022/8/18
 * @since 3.0.0
 */
public class UserTypeTypeHandler extends BaseTypeHandler<UserTypeEnum> {

    private static final Map<String, UserTypeEnum> DATA_MAP = Arrays.stream(UserTypeEnum.values())
            .collect(Collectors.toMap(UserTypeEnum::getValue, item -> item));

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, UserTypeEnum userTypeEnum, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, userTypeEnum.getValue());
    }

    @Override
    public UserTypeEnum getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return Optional.ofNullable(resultSet.getString(s))
                .map(DATA_MAP::get)
                .orElse(null);
    }

    @Override
    public UserTypeEnum getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return Optional.ofNullable(resultSet.getString(i))
                .map(DATA_MAP::get)
                .orElse(null);
    }

    @Override
    public UserTypeEnum getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return Optional.ofNullable(callableStatement.getString(i))
                .map(DATA_MAP::get)
                .orElse(null);
    }
}
