package com.smart.db.generator.mybatis.type;

import com.smart.db.generator.constants.RuleTriggerEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2021/6/11 11:16
 * @since 1.0
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
public class RuleTriggerTypeHandler extends BaseTypeHandler<List<RuleTriggerEnum>> {

    private static final String SPLIT = ",";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<RuleTriggerEnum> parameter, JdbcType jdbcType) throws SQLException {
        if (!CollectionUtils.isEmpty(parameter)) {
            final String value = parameter.stream()
                    .map(Enum::name)
                    .collect(Collectors.joining(SPLIT));
            ps.setString(i, value);
        }
    }

    @Override
    public List<RuleTriggerEnum> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        final String value = rs.getString(columnName);
        if (StringUtils.isBlank(value)) {
            return Collections.emptyList();
        }
        return this.parse(value);
    }

    @Override
    public List<RuleTriggerEnum> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        final String value = rs.getString(columnIndex);
        return StringUtils.isBlank(value) ? null : parse(value);
    }

    @Override
    public List<RuleTriggerEnum> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        final String value = cs.getString(columnIndex);
        return StringUtils.isBlank(value) ? null : parse(value);
    }

    private List<RuleTriggerEnum> parse(@NonNull String value) {
        return Arrays.stream(value.split(SPLIT))
                .map(RuleTriggerEnum::valueOf)
                .toList();
    }
}
