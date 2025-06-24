package com.smart.framework.crud.pagehelper.parser;

import com.github.pagehelper.parser.defaults.DefaultCountSqlParser;
import net.sf.jsqlparser.statement.select.WithItem;

import java.util.List;

/**
 * 重写DefaultCountSqlParser
 * 1、解决jsqlparser版本冲突问题：net.sf.jsqlparser.statement.select.WithItem#getSelect()
 * @author shizhongming
 * 2025/6/18 16:58
 * @since 5.0.0
 */
public class SmartCountSqlParser extends DefaultCountSqlParser {

    /**
     * 处理WithItem
     *
     * @param withItemsList withItemsList
     */
    @Override
    public void processWithItemsList(List<WithItem> withItemsList) {
        if (withItemsList != null && !withItemsList.isEmpty()) {
            for (WithItem<?> item : withItemsList) {
                if (item.getSelect() != null && !keepSubSelectOrderBy()) {
                    processSelect(item.getSelect());
                }
            }
        }
    }
}
