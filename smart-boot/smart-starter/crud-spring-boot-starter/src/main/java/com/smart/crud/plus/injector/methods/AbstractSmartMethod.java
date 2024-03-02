package com.smart.crud.plus.injector.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.smart.crud.plus.logic.LogicKeyStrategy;
import com.smart.crud.plus.metadata.TableLogicDeleteFieldInfo;
import com.smart.crud.utils.CrudUtils;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author shizhongming
 * 2023/10/31 14:09
 * @since 3.0.0
 */
public abstract class AbstractSmartMethod extends AbstractMethod {
    @Serial
    private static final long serialVersionUID = -9045966947653971679L;

    private static final String FORMAT_STRING = "%s = %s";

    /**
     * @param methodName 方法名
     * @since 3.5.0
     */
    protected AbstractSmartMethod(String methodName) {
        super(methodName);
    }

    /**
     * 获取设置 逻辑删除key SQL
     * @param tableInfo table info
     * @return SQL
     */
    protected String sqlLogicDeleteFieldSet(TableInfo tableInfo, final String prefix) {
        TableLogicDeleteFieldInfo logicKeyField = Objects.requireNonNull(CrudUtils.getTableLogicKeyField(tableInfo));
        List<String> sqlList = new ArrayList<>(8);
        // 设置逻辑删除
        sqlList.add(tableInfo.getLogicDeleteSql(false, false));
        // 设置逻辑删除的key
        if (logicKeyField.getTableLogicKey() != null && LogicKeyStrategy.ID.equals(logicKeyField.getTableLogicKey().strategy())) {
            sqlList.add(String.format(FORMAT_STRING, logicKeyField.getDeleteKeyFieldInfo().getColumn(), tableInfo.getKeyColumn()));
        }
        // 设置逻辑删除的相关字段
        String collect = Stream.of(logicKeyField.getDeleteTimeFieldInfo(), logicKeyField.getDeleteByFieldInfo(), logicKeyField.getDeleteUserIdFieldInfo())
                .filter(Objects::nonNull)
                .map(item -> item.getSqlSet(prefix))
                .filter(Objects::nonNull)
                .collect(Collectors.joining(NEWLINE));
        if (StringUtils.hasText(collect)) {
            sqlList.add(collect);
        }
        return String.join(", \n", sqlList);
    }
}
