package com.smart.crud.plus.injector.methods;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.smart.crud.plus.logic.LogicKeyStrategy;
import com.smart.crud.plus.metadata.SmartTableInfo;
import com.smart.crud.plus.metadata.TableLogicDeleteInfo;
import com.smart.crud.utils.CrudUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.core.toolkit.StringPool.NEWLINE;

/**
 * @author shizhongming
 * 2023/10/31 14:09
 * @since 3.0.0
 */
public interface AbstractSmartMethod {

    String FORMAT_STRING = "%s = %s";

    /**
     * 获取设置 逻辑删除key SQL
     * @param tableInfo table info
     * @return SQL
     */
    default String sqlLogicDeleteFieldSet(TableInfo tableInfo, final String prefix, boolean ignoreIf) {
        SmartTableInfo smartTableInfo = CrudUtils.getTableInfo(tableInfo.getEntityType());
        TableLogicDeleteInfo logicDeleteInfo = Objects.requireNonNull(smartTableInfo.getLogicDeleteInfo());
        List<String> sqlList = new ArrayList<>(8);
        // 设置逻辑删除
        sqlList.add(tableInfo.getLogicDeleteSql(false, false));
        // 设置逻辑删除的key
        if (logicDeleteInfo.getDeleteKeyFieldInfo() != null && LogicKeyStrategy.ID.equals(logicDeleteInfo.getLogicKeyStrategy())) {
            sqlList.add(String.format(FORMAT_STRING, logicDeleteInfo.getDeleteKeyFieldInfo().getColumn(), tableInfo.getKeyColumn()));
        }
        // 设置逻辑删除的相关字段
        String collect = logicDeleteInfo.getFillFieldInfoList().stream()
                .map(item -> item.getSqlSet(ignoreIf, prefix))
                .filter(Objects::nonNull)
                .collect(Collectors.joining(NEWLINE));
        if (StringUtils.hasText(collect)) {
            sqlList.add(collect);
        }
        return String.join(", \n", sqlList);
    }
}
