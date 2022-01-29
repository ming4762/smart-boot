package com.smart.db.generator.document;

import com.google.common.collect.Lists;
import com.smart.commons.core.document.DocumentVO;
import com.smart.db.generator.constants.FromControlTypeEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2021/9/9 11:54
 * @since 1.0
 */
public class DbGeneratorDocumentCreator {

    private static final String TYPE_STRING = "String";

    private static final String TYPE_BOOLEAN = "Boolean";

    private DbGeneratorDocumentCreator() {
    }

    public static List<DocumentVO> createDocument() {
        return Lists.newArrayList(
                new DocumentVO("className", "实体类类名", TYPE_STRING, null, "数据库表名转驼峰", false),
                new DocumentVO("description", "描述", TYPE_STRING, null, "数据库表注释", true),
                new DocumentVO("packages", "包名", TYPE_STRING, null, null, false),
                new DocumentVO("extPackages", "ext包名", TYPE_STRING, null, null, true),
                new DocumentVO("controllerBasePath", "controller请求路径", TYPE_STRING, null, null, true),
                new DocumentVO("mainTable", "表配置", "Object", null, null, false, createTableDocument()),
                new DocumentVO("addendumTableList", "附表配置，请参考表配置", "List", null, null, true)
        );
    }

    private static List<DocumentVO> createTableDocument() {
        return Lists.newArrayList(
                new DocumentVO("configName", "配置名称", "String", null, null, false),

                new DocumentVO("idField", "主键字段配置信息", "Object", null, null, true, createPageConfigDocument()),

                new DocumentVO("className", "类名", "String", null, null, false),
                new DocumentVO("tableName", "数据库表名称", "String", null, null, false),
                new DocumentVO("type", "数据库类型", "String", null, null, false),
                new DocumentVO("showCheckbox", "是否显示复选框", TYPE_BOOLEAN, null, null, false),
                new DocumentVO("page", "是否分页", TYPE_BOOLEAN, null, null, false),
                new DocumentVO("invented", "是否是虚拟表格", TYPE_BOOLEAN, null, null, false),
                new DocumentVO("formColNum", "表单列数", "Int", null, null, false),
                new DocumentVO("searchColNum", "搜索表单列数", "Int", null, null, false),
                new DocumentVO("remark", "备注", "String", null, null, true),
                new DocumentVO("remarks", "数据库表备注", "String", null, null, true),
                new DocumentVO("columnSort", "列顺序是否可配置", TYPE_BOOLEAN, null, null, false),
                new DocumentVO("rowButtonType", "行按钮类型", "Enum", Lists.newArrayList("NONE：无行按钮", "SINGLE：单个按钮", "MORE：下拉按钮", "TEXT：文本按钮"), null, false),

                new DocumentVO("hasId", "数据库表是否有主键", TYPE_BOOLEAN, Lists.newArrayList("true", "false"), "false", false),
                new DocumentVO("relatedColumn", "关联字段（用户select-table等）", "String", null, null, true),
                new DocumentVO("modelClassImportList", "引入的类型", "List<String>", null, null, false),
                new DocumentVO("codePageConfigList", "页面配置信息", "List<Object>", null, null, false, createPageConfigDocument()),
                new DocumentVO("codeFormConfigList", "表单配置信息", "List<Object>", null, null, false, createFormConfigDocument()),
                new DocumentVO("codeSearchConfigList", "搜索form配置信息", "List<Object>", null, null, false, createSearchConfigDocument()),
                new DocumentVO("leftButtonList", "左侧按钮配置信息", "List<String>", null, null, false),
                new DocumentVO("rightButtonList", "右侧按钮配置信息", "List<String>", null, null, false),
                new DocumentVO("rowButtonList", "行按钮配置信息", "List<String>", null, null, false)
        );
    }

    /**
     * 创建页面配置文档
     * @return 页面配置文件
     */
    private static List<DocumentVO> createPageConfigDocument() {
        List<DocumentVO> documentList = Lists.newArrayList(
                new DocumentVO("idAnnotation", "是否使用ID注解（mybatis）", "Boolean", null, "false", false),
                new DocumentVO("remarks", "表备注", "String", null, null, true),
                new DocumentVO("title", "标题", "String", null, null, false),
                new DocumentVO("sortable", "是否可排序", "Boolean", null, "false", false),
                new DocumentVO("fixed", "冻结列", "String", Lists.newArrayList("right", "left"), null, true),
                new DocumentVO("width", "列宽度", "number", null, null, false),
                new DocumentVO("align", "列对齐方式", "String", Lists.newArrayList("left", "center", "right"), null, false),

                new DocumentVO("resizable", "列宽度是否可调", "Boolean", null, "false", false),
                new DocumentVO("visible", "是否渲染到页面", "Boolean", null, null, false),
                new DocumentVO("hidden", "是否隐藏", "Boolean", null, null, false),
                new DocumentVO("editable", "是否可编辑", "Boolean", null, null, false),
                new DocumentVO("format", "格式化", "String", null, null, true)
        );
        documentList.addAll(createConfigCommonDocument());
        return documentList;
    }

    /**
     * 创建添加修改表单配置文件
     * @return 添加修改表单文档
     */
    private static List<DocumentVO> createFormConfigDocument() {
        List<DocumentVO> documentList = Lists.newArrayList(
                new DocumentVO("selectTable", "下拉表格配置，参考表格配置信息", "Object", null, null, true),
                new DocumentVO("ruleList", "验证规则", "List", null, null, true)
        );
        documentList.addAll(createFormConfigCommonDocument());
        return documentList;
    }

    /**
     * 创建搜索配置
     * @return 搜索配置
     */
    private static List<DocumentVO> createSearchConfigDocument() {
        List<DocumentVO> documentList = Lists.newArrayList(
                new DocumentVO("selectTable", "下拉表格配置，参考表格配置信息", "Object", null, null, true)
        );
        documentList.addAll(createFormConfigCommonDocument());
        return documentList;
    }


    /**
     * 创建表单配置文件（添加修改表单、搜索表单 通用）
     * @return 表单配置文件
     */
    private static List<DocumentVO> createFormConfigCommonDocument() {
        List<DocumentVO> documentList = Lists.newArrayList(
                new DocumentVO("remarks", "数据库列备注", "String", null, null, false),
                new DocumentVO("title", "标题", "String", null, null, false),
                new DocumentVO("readonly", "是否只读", "Boolean", null, "false", false),
                new DocumentVO("hidden", "是否隐藏", "Boolean", null, null, false),

                new DocumentVO("controlType", "控件类型", "String", Arrays.stream(FromControlTypeEnum.values()).map(Enum::name).collect(Collectors.toList()), null, false),
                new DocumentVO("seq", "序号", "Integer", null, null, false),

                new DocumentVO("useTableSearch", "是否查询数据库", "Boolean", null, "false", true),
                new DocumentVO("tableName", "查询数据库表名", "String", null, null, true),
                new DocumentVO("keyColumnName", "查询数据库key列", "String", null, null, true),
                new DocumentVO("valueColumnName", "查询数据库value列", "String", null, null, true),
                new DocumentVO("tableWhere", "查询条件", "String", null, null, true),

                new DocumentVO("visible", "是否渲染", "Boolean", null, "true", false),
                new DocumentVO("used", "是否作为表单项", "Boolean", null, "true", false)
        );
        documentList.addAll(createConfigCommonDocument());
        return documentList;
    }

    private static List<DocumentVO> createConfigCommonDocument() {
        return Lists.newArrayList(
                new DocumentVO("columnName", "数据库列名称", "String", null, null, false),
                new DocumentVO("javaType", "Java类型（限定名）", "String", null, null, false),
                new DocumentVO("simpleJavaType", "Java类型（简称）", "String", null, null, false),
                new DocumentVO("javaProperty", "java属性名", "String", null, null, false)
        );
    }

}
