package com.smart.framework.crud.plus.inner;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.inner.BaseMultiTableInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.smart.framework.crud.plus.handlers.SmartTenantLineHandler;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;
import org.apache.ibatis.mapping.SqlCommandType;

import java.util.List;

/**
 * 增强mybatis plus 租户拦截器
 * @author shizhongming
 * 2024/4/11 10:57
 * @since 3.0.0
 */
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SmartTenantLineInnerInterceptor extends TenantLineInnerInterceptor {

    private final SmartTenantLineHandler smartTenantLineHandler;

    @Override
    protected void processInsert(Insert insert, int index, String sql, Object obj) {
        String tableName = insert.getTable().getName();
        if (this.smartTenantLineHandler.ignoreTable(tableName, SqlCommandType.INSERT)) {
            return;
        }
        List<Column> columns = insert.getColumns();
        if (CollectionUtils.isEmpty(columns)) {
            // 针对不给列名的insert 不处理
            return;
        }
        String tenantIdColumn = smartTenantLineHandler.getTenantIdColumn(tableName);
        if (smartTenantLineHandler.ignoreInsert(columns, tenantIdColumn)) {
            // 针对已给出租户列的insert 不处理
            return;
        }

        columns.add(new Column(tenantIdColumn));
        Expression tenantId = smartTenantLineHandler.getTenantId();
        // fixed gitee pulls/141 duplicate update
        List<UpdateSet> duplicateUpdateColumns = insert.getDuplicateUpdateSets();
        if (CollectionUtils.isNotEmpty(duplicateUpdateColumns)) {
            duplicateUpdateColumns.add(new UpdateSet(new Column(tenantIdColumn), tenantId));
        }

        Select select = insert.getSelect();
        if (select instanceof PlainSelect) { //fix github issue 4998  修复升级到4.5版本的问题
            this.processInsertSelect(tableName, select, (String) obj);
        } else if (insert.getValues() != null) {
            // fixed github pull/295
            Values values = insert.getValues();
            ExpressionList<Expression> expressions = (ExpressionList<Expression>) values.getExpressions();
            if (expressions instanceof ParenthesedExpressionList) {
                expressions.addExpression(tenantId);
            } else {
                if (CollectionUtils.isNotEmpty(expressions)) {//fix github issue 4998 jsqlparse 4.5 批量insert ItemsList不是MultiExpressionList 了，需要特殊处理
                    int len = expressions.size();
                    for (int i = 0; i < len; i++) {
                        Expression expression = expressions.get(i);
                        if (expression instanceof ParenthesedExpressionList) {
                            ((ParenthesedExpressionList<Expression>) expression).addExpression(tenantId);
                        } else {
                            expressions.add(tenantId);
                        }
                    }
                } else {
                    expressions.add(tenantId);
                }
            }
        } else {
            throw ExceptionUtils.mpe("Failed to process multiple-table update, please exclude the tableName or statementId");
        }
    }


    @Override
    protected void processUpdate(Update update, int index, String sql, Object obj) {
        Table table = update.getTable();
        if (this.smartTenantLineHandler.ignoreTable(table.getName(), SqlCommandType.UPDATE)) {
            // 过滤退出执行
            return;
        }
        List<UpdateSet> sets = update.getUpdateSets();
        if (!CollectionUtils.isEmpty(sets)) {
            sets.forEach(us -> us.getValues().forEach(ex -> {
                if (ex instanceof Select select) {
                    processSelectBody(select, (String) obj);
                }
            }));
        }
        update.setWhere(this.andExpression(table, update.getWhere(), (String) obj));
    }


    @Override
    protected void processDelete(Delete delete, int index, String sql, Object obj) {
        if (this.smartTenantLineHandler.ignoreTable(delete.getTable().getName(), SqlCommandType.DELETE)) {
            // 过滤退出执行
            return;
        }
        delete.setWhere(this.andExpression(delete.getTable(), delete.getWhere(), (String) obj));
    }

    /**
     * 处理 insert into select
     * <p>
     * 进入这里表示需要 insert 的表启用了多租户,则 select 的表都启动了
     *
     * @param tableName 表名
     * @param selectBody SelectBody
     */
    protected void processInsertSelect(String tableName, Select selectBody, final String whereSegment) {
        if(selectBody instanceof PlainSelect plainSelect){
            FromItem fromItem = plainSelect.getFromItem();
            if (fromItem instanceof Table) {
                // fixed gitee pulls/141 duplicate update
                processPlainSelect(plainSelect, whereSegment);
                appendSelectItem(tableName, plainSelect.getSelectItems());
            } else if (fromItem instanceof Select subSelect) {
                appendSelectItem(tableName, plainSelect.getSelectItems());
                processInsertSelect(tableName, subSelect, whereSegment);
            }
        } else if(selectBody instanceof ParenthesedSelect parenthesedSelect){
            processInsertSelect(tableName, parenthesedSelect.getSelect(), whereSegment);

        }
    }

    protected void appendSelectItem(String tableName, List<SelectItem<?>> selectItems) {
        if (CollectionUtils.isEmpty(selectItems)) {
            return;
        }
        if (selectItems.size() == 1) {
            SelectItem<?> item = selectItems.get(0);
            Expression expression = item.getExpression();
            if (expression instanceof AllColumns) {
                return;
            }
        }
        selectItems.add(new SelectItem<>(new Column(this.smartTenantLineHandler.getTenantIdColumn(tableName))));
    }

    /**
     * 租户字段别名设置
     * <p>tenantId 或 tableAlias.tenantId</p>
     *
     * @param table 表对象
     * @return 字段
     */
    @Override
    protected Column getAliasColumn(Table table) {
        StringBuilder column = new StringBuilder();
        // todo 该起别名就要起别名,禁止修改此处逻辑
        if (table.getAlias() != null) {
            column.append(table.getAlias().getName()).append(StringPool.DOT);
        }
        column.append(this.smartTenantLineHandler.getTenantIdColumn(table.getName()));
        return new Column(column.toString());
    }

    /**
     * 构建租户条件表达式
     *
     * @param table        表对象
     * @param where        当前where条件
     * @param whereSegment 所属Mapper对象全路径（在原租户拦截器功能中，这个参数并不需要参与相关判断）
     * @return 租户条件表达式
     * @see BaseMultiTableInnerInterceptor#buildTableExpression(Table, Expression, String)
     */
    @Override
    public Expression buildTableExpression(final Table table, final Expression where, final String whereSegment) {
        if (this.smartTenantLineHandler.ignoreTable(table.getName(), SqlCommandType.SELECT)) {
            return null;
        }
        return new EqualsTo(getAliasColumn(table), this.smartTenantLineHandler.getTenantId());
    }
}
