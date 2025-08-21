package com.smart.framework.tool.database.utils;

import com.smart.framework.tool.database.constants.SqlTypeEnum;
import com.smart.framework.tool.database.exception.SmartSqlCheckerException;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.execute.Execute;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.merge.Merge;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * SQL安全检查工具类
 * @author shizhongming
 * 2025/8/19 19:18
 * @since 5.0.0
 */
public class SqlSecurityCheckerUtils {

    private SqlSecurityCheckerUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * SQL分号检查
     */
    private static final Pattern COMMENT_OR_SEMICOLON = Pattern.compile("(--|/\\*|\\*/|;)");
    /**
     * 危险语句列表
     */
    private static final List<Class<?>> DANGEROUS_STATEMENT_CLASSES = List.of(
            CreateTable.class,
            Drop.class,
            Alter.class,
            Truncate.class,
            Merge.class,
            Execute.class
    );
    private static final Set<String> DANGEROUS_FUNCTIONS = new HashSet<>(Arrays.asList(
            "sleep", "benchmark", "load_file", "xp_cmdshell", "pg_sleep"
    ));
    private static final Map<Class<? extends Statement>, SqlTypeEnum> SQL_TYPE_MAP = Map.ofEntries(
            Map.entry(Select.class, SqlTypeEnum.SELECT),
            Map.entry(Insert.class, SqlTypeEnum.INSERT),
            Map.entry(Update.class, SqlTypeEnum.UPDATE),
            Map.entry(Delete.class, SqlTypeEnum.DELETE)
    );

    /**
     * 校验SQL是否安全
     * @param sql SQL语句
     */
    public static void validate(String sql) {
        validate(sql, null, true);
    }

    /**
     * 校验SQL是否安全
     * @param sql SQL语句
     * @param allowSqlTypes 允许的SQL类型, 为空则不校验
     * @param allowSelectAll 是否允许select *
     */
    public static void validate(String sql, List<SqlTypeEnum> allowSqlTypes, boolean allowSelectAll) {
        if (!StringUtils.hasText(sql)) {
            return;
        }
        if (COMMENT_OR_SEMICOLON.matcher(sql).find()) {
            throw new SmartSqlCheckerException("contains comment or semicolon which may indicate multiple statements or injection");
        }
        Statements statements;
        try {
            statements = CCJSqlParserUtil.parseStatements(sql);
        } catch (JSQLParserException e) {
            throw new SmartSqlCheckerException("sql parse error", e);
        }
        // 校验是否存在多语句
        if (statements.size() > 1) {
            throw new SmartSqlCheckerException("multiple statements not allowed");
        }
        Statement statement = statements.getFirst();
        String simpleName = statement.getClass().getSimpleName().toLowerCase();
        // 校验是否存在危险语句
        if (DANGEROUS_STATEMENT_CLASSES.contains(statement.getClass())) {
            throw new SmartSqlCheckerException("ddl/dcl/execute statements are not allowed: " + simpleName);
        }
        SqlTypeEnum sqlType = getSqlType(statement);
        if (sqlType == null) {
            throw new SmartSqlCheckerException("sql type not supported: " + simpleName);
        }
        if (!CollectionUtils.isEmpty(allowSqlTypes) && !allowSqlTypes.contains(sqlType)) {
            throw new SmartSqlCheckerException("sql type not allowed: " + sqlType);
        }
        // 对SELECT 做更细粒度检查（UNION、SELECT *、函数、子查询）
        if (statement instanceof Select select) {
            validateSelectStatement(select, allowSelectAll);
        }
    }

    /**
     * 校验SELECT语句是否安全
     * @param select 查询语句
     * @param allowSelectAll 是否允许select *
     */
    private static void validateSelectStatement(Select select, boolean allowSelectAll) {
        if (!(select instanceof PlainSelect plainSelect)) {
            return;
        }
        if (!allowSelectAll) {
            validateSelectAll(plainSelect);
        }
        // 校验是否有危险函数
        Expression where = plainSelect.getWhere();
        if (where != null) {
            checkDangerExpression(where);
        }
        plainSelect.getSelectItems().forEach(item -> checkDangerExpression(item.getExpression()));
    }

    /**
     * 校验是否存在select *
     * @param plainSelect 查询语句
     */
    private static void validateSelectAll(PlainSelect plainSelect) {
        ExpressionVisitorAdapter<Void> selectItemVisitor = new ExpressionVisitorAdapter<>() {
            @Override
            public void visit(AllColumns allColumns) {
                throw new SmartSqlCheckerException("SELECT * (or table.*) not allowed; prefer explicit columns");
            }

            @Override
            public void visit(AllTableColumns allTableColumns) {
                throw new SmartSqlCheckerException("SELECT * (or table.*) not allowed; prefer explicit columns");
            }
        };
        plainSelect.getSelectItems().forEach(item -> item.accept(selectItemVisitor, null));
    }

    /**
     * 检查表达式是否包含危险函数
     */
    private static void checkDangerExpression(Expression expr) {
        expr.accept(new ExpressionDeParser() {
            @Override
            public void visit(Function function) {
                String name = function.getName();
                if (name != null && DANGEROUS_FUNCTIONS.contains(name.toLowerCase())) {
                    throw new SmartSqlCheckerException("dangerous function used: " + name);
                }
                super.visit(function);
            }
        });
    }

    /**
     * 获取SQL类型
     * @param statement SQL语句
     * @return SQL类型
     */
    private static SqlTypeEnum getSqlType(Statement statement) {
        for (Map.Entry<Class<? extends Statement>, SqlTypeEnum> entry : SQL_TYPE_MAP.entrySet()) {
            Class<? extends Statement> entryKey = entry.getKey();
            if (statement.getClass().equals(entryKey) || entryKey.isAssignableFrom(statement.getClass())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
