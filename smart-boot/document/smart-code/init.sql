
-- 插入菜单信息
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1456679770128418, 0, '代码生成器', '10', '{menu.generator.main}', 200, sysdate(), 1, null, 1, null, null, 1, 0, 0, null, null, 'DeploymentUnitOutlined');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1456680244084770, 1456679770128418, 'DB管理', '20', '{menu.generator.db.manager}', 210, sysdate(), 1, null, 1, '/code/databaseList', null, 1, 0, 0, null, null, 'DatabaseOutlined');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1456862505467938, 1456679770128418, '模板管理', '20', '{menu.generator.template.manager}', 220, sysdate(), 1, null, null, '/code/templateList', null, 1, 0, 0, null, null, 'ReadOutlined');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1458264340430882, 1456679770128418, '代码管理', '20', '{menu.generator.code.manager}', 230, sysdate(), 1, null, null, '/code/codeList', null, 1, 0, 0, null, null, 'FileMarkdownOutlined');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1459748195008546, 1456679770128418, '代码生成器文档', '20', '', 240, sysdate(), 1, null, null, '/code/document', null, 1, 0, 0, null, null, 'FileSearchOutlined');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1465870465368098, 1456680244084770, '添加数据库连接', '30', null, 10, sysdate(), 1, null, null, '/db/connection/save', 'db:connection:save', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1465870637334562, 1456680244084770, '修改数据库连接', '30', null, 20, sysdate(), 1, null, null, '/db/connection/update', 'db:connection:update', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1465870803009570, 1456680244084770, '删除数据库连接', '30', null, 30, sysdate(), 1, null, null, '/db/connection/batchDeleteById', 'db:connection:delete', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1465870983364642, 1456680244084770, '设置数据库连接用户组', '30', null, 40, sysdate(), 1, null, null, '/db/connection/setUserGroup', 'db:connection:setUserGroup', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1465871178399778, 1456862505467938, '添加模板', '30', null, 10, sysdate(), 1, null, null, '/db/code/template/save', 'db:template:save', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1465871272771618, 1456862505467938, '修改模板', '30', null, 20, sysdate(), 1, null, null, '/db/code/template/update', 'db:template:update', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1465871436349474, 1456862505467938, '删除模板', '30', null, 30, sysdate(), 1, null, null, '/db/code/template/batchDeleteById', 'db:template:delete', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1465871595733026, 1456862505467938, '设置模板对应用户组', '30', null, 40, sysdate(), 1, null, null, '/db/code/template/saveTemplateUserGroup', 'db:template:saveTemplateUserGroup', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1465871794962466, 1456862505467938, '添加模板组', '30', null, 2, sysdate(), 1, null, null, '/db/code/template/saveUpdateGroup', 'db:templateGroup:save', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1465871975317538, 1456862505467938, '修改模板组', '30', null, 4, sysdate(), 1, null, null, '/db/code/template/saveUpdateGroup', 'db:templateGroup:update', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1465872055009314, 1456862505467938, '删除模板组', '30', null, 6, sysdate(), 1, null, null, '/db/code/template/deleteGroupById', 'db:templateGroup:delete', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1465880647041058, 1458264340430882, '添加代码配置', '30', null, 10, sysdate(), 1, null, null, '/db/code/main/save', 'db:codeConfig:save', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1465880774967330, 1458264340430882, '删除代码配置', '30', null, 20, sysdate(), 1, null, null, '/db/code/main/batchDeleteById', 'db:codeConfig:delete', 1, 0, 0, 'POST', null, null);

-- 国际化信息
INSERT INTO sys_i18n_group (group_id, group_name, create_time, create_user_id, update_time, update_user_id, seq) VALUES (1457225249849378, '代码生成器菜单', sysdate(), 1, null, null, 150);

INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1457225335832610, 'backstage', 'menu.generator.main', 1457225249849378, null, 1, null, null, 1, 0, 10, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1458109654499362, 'backstage', 'menu.generator.db.manager', 1457225249849378, '代码生成器-DB管理', 1, null, null, 1, 0, 10, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1458111776817186, 'backstage', 'menu.generator.template.manager', 1457225249849378, '代码生成器-模板管理', 1, null, 1, 1, 0, 20, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1458437825232930, 'backstage', 'menu.generator.code.manager', 1457225249849378, '代码生成器-代码配置', 1, null, null, 1, 0, 40, sysdate());


INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1458108704489506, 'en-US', 'Code Generator', sysdate(), 1, 1, 0, 1457225335832610);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1458108635283490, 'zh-CN', '代码生成器', sysdate(), 1, 1, 0, 1457225335832610);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1458109702733858, 'en-US', 'DB Manager', sysdate(), 1, 1, 0, 1458109654499362);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1458109683859490, 'zh-CN', 'DB管理', sysdate(), 1, 1, 0, 1458109654499362);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1458111818760226, 'en-US', 'Template Manager', sysdate(), 1, 1, 0, 1458111776817186);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1458111801983010, 'zh-CN', '模板管理', sysdate(), 1, 1, 0, 1458111776817186);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1458437869273122, 'en-US', 'Code Config', sysdate(), 1, 1, 0, 1458437825232930);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1458437848301602, 'zh-CN', '代码配置', sysdate(), 1, 1, 0, 1458437825232930);

-- 数据库连接信息表
drop table if exists `db_connection`;
create table db_connection
(
    id              bigint               not null
        primary key,
    connection_name varchar(255)         not null comment '连接名字',
    database_name   varchar(100)         not null comment '数据库名称',
    url             varchar(255)         not null comment '连接地址',
    username        varchar(255)         not null comment '用户名',
    password        varchar(255)         not null comment '密码',
    table_schema    varchar(50)          null,
    create_user_id  bigint               null comment '创建人员',
    create_time     datetime             not null comment '创建时间',
    update_user_id  bigint               null comment '更新人员ID',
    update_time     datetime             null comment '更新时间',
    database_type   varchar(20)          not null comment '数据库类型',
    project         varchar(255)         not null comment '项目',
    seq             int        default 1 not null,
    use_yn          tinyint(1) default 1 not null comment '是否启用',
    delete_yn       tinyint(1) default 0 not null comment '是否删除'
)
    comment '数据库连接表';

-- 按钮配置表
drop table if exists `db_code_button_config`;
create table db_code_button_config
(
    id         bigint      not null comment 'ID'
        primary key,
    related_id bigint      not null comment '关联ID',
    ident      char(2)     not null comment '标识
10：左侧按钮
20：右侧按钮
30：行按钮',
    button     varchar(20) not null comment '按钮',
    seq        int         not null
);

-- 数据库连接用户组关联关系表
drop table if exists `db_code_connection_user_group`;
create table db_code_connection_user_group
(
    connection_id  bigint   not null,
    user_group_id  bigint   not null,
    create_time    datetime not null,
    create_user_id bigint   not null,
    primary key (connection_id, user_group_id)
);

-- 表单配置表
drop table if exists `db_code_form_config`;
create table db_code_form_config
(
    id                bigint               not null
        primary key,
    main_id           bigint               not null,
    column_name       varchar(255)         not null,
    remarks           varchar(1000)        null,
    title             varchar(1000)        null,
    readonly          tinyint(1)           not null,
    hidden            tinyint(1)           not null,
    control_type      varchar(50)          not null,
    visible           tinyint(1) default 1 not null,
    create_user_id    bigint               null,
    create_time       datetime             null,
    update_user_id    bigint               null,
    update_time       datetime             null,
    seq               int        default 1 not null,
    use_table_search  tinyint(1)           null comment '是否使用表查询',
    table_name        varchar(255)         null comment '查询表名',
    key_column_name   varchar(255)         null comment 'key对应的字段名',
    value_column_name varchar(255)         null comment 'value对应的字段',
    table_where       text                 null comment '查询条件',
    used              tinyint(1) default 1 not null comment '是否使用',
    java_property     varchar(255)         null,
    ext_type          varchar(255)         null,
    java_type         varchar(255)         null,
    simple_java_type  varchar(255)         null
)
    comment '表单配置信息';

-- 主配置表
drop table if exists `db_code_main`;
create table db_code_main
(
    id              bigint                      not null
        primary key,
    connection_id   bigint                      not null comment '数据库连接ID',
    config_name     varchar(255)                not null comment '配置名称',
    class_name      varchar(2555)               not null comment '类名',
    table_name      varchar(255)                not null comment '表名',
    type            char(2)                     not null comment '表类型
10：单表
20：主表
30：附表',
    show_checkbox   tinyint(1)                  not null comment '是否显示复选框',
    page            tinyint(1)                  not null comment '是否分页',
    invented        tinyint(1)                  not null comment '是否虚拟滚动',
    column_sort     tinyint(1)   default 0      not null comment '列顺序是否可调',
    form_col_num    int                         not null comment '表单列数',
    search_col_num  int                         not null comment '搜索表单列数',
    remark          varchar(255)                null comment '备注',
    create_user_id  bigint                      null,
    create_time     datetime                    not null,
    update_user_id  int                         null,
    update_time     datetime                    null,
    table_remark    varchar(1000)               null,
    row_button_type varchar(10)  default 'none' not null comment '行按钮类型：
none:无
single：统一
more：多个
text：文本',
    i18n_prefix     varchar(255) default ''     not null comment '国际化前缀'
)
    comment '代码生成信息';

-- 页面配置表
drop table if exists `db_code_page_config`;
create table db_code_page_config
(
    id               bigint               not null
        primary key,
    main_id          bigint               not null comment '主配置ID',
    column_name      varchar(255)         not null comment '列名',
    java_property    varchar(255)         not null,
    java_type        varchar(255)         not null,
    type_name        varchar(255)         not null,
    column_size      int                  not null,
    decimal_digits   int                  null,
    column_def       varchar(255)         null,
    nullable         tinyint(1)           null,
    remarks          varchar(1000)        null,
    primary_key      tinyint(1)           not null,
    indexed          tinyint(1)           not null,
    table_name       varchar(255)         not null,
    ext_type         varchar(255)         null,
    title            varchar(255)         null,
    sortable         tinyint(1)           not null,
    fixed            varchar(50)          null,
    width            varchar(255)         null,
    align            varchar(50)          null,
    resizable        tinyint(1)           not null,
    visible          tinyint(1) default 1 not null,
    hidden           tinyint(1)           not null,
    format           varchar(1000)        null,
    create_user_id   bigint               null,
    create_time      datetime             null,
    update_user_id   bigint               null,
    update_time      datetime             null,
    simple_java_type varchar(255)         not null,
    seq              int        default 1 not null,
    editable         tinyint(1) default 0 null comment '是否可编辑'
)
    comment '页面配置信息';

-- 表关联关系表
drop table if exists `db_code_related_table`;
create table db_code_related_table
(
    main_id        bigint       not null comment '主表ID',
    addendum_id    bigint       not null comment '附表ID',
    ident          varchar(20)  not null comment '标识：
form：form下拉table关联
search：search下拉table关联
main_table：主表关联',
    related_column varchar(255) not null comment '关联列',
    type           varchar(50)  null comment '类型：
select_table:下拉表格',
    seq            int          null
)
    comment '表关联信息';

-- form rule配置表
drop table if exists `db_code_rule_config`;
create table db_code_rule_config
(
    id           bigint       not null
        primary key,
    rule_type    varchar(255) not null comment '校验类型',
    rule_trigger varchar(255) not null comment '触发时机',
    len          int          null comment 'len',
    max          int          null comment '最大程度',
    min          int          null comment '最小长度',
    message      varchar(255) not null comment '校验文案',
    pattern      varchar(500) null comment '正则表达式',
    seq          int          not null comment '序号',
    ident        varchar(20)  not null comment '标识',
    relation_id  bigint       not null comment '关联ID'
);

-- 搜索配置表
drop table if exists `db_code_search_config`;
create table db_code_search_config
(
    id                bigint               not null
        primary key,
    main_id           bigint               not null,
    column_name       varchar(255)         not null,
    remarks           varchar(1000)        null,
    title             varchar(1000)        null,
    readonly          tinyint(1)           not null,
    hidden            tinyint(1)           not null,
    visible           tinyint(1) default 0 not null,
    control_type      varchar(50)          not null,
    search_symbol     varchar(50)          null comment '搜索符号',
    seq               int        default 1 not null,
    use_table_search  tinyint(1)           null comment '是否使用表查询',
    table_name        varchar(255)         null comment '查询表名',
    key_column_name   varchar(255)         null comment 'key对应的字段名',
    value_column_name varchar(255)         null comment 'value对应的字段',
    table_where       text                 null comment '查询条件',
    create_user_id    bigint               null,
    create_time       datetime             null,
    update_user_id    bigint               null,
    update_time       datetime             null,
    used              tinyint(1) default 1 not null,
    java_property     varchar(255)         null,
    ext_type          varchar(255)         null,
    java_type         varchar(255)         null,
    simple_java_type  varchar(255)         null
)
    comment '搜索配置信息表';

-- 模板配置表
drop table if exists `db_code_template`;
create table db_code_template
(
    template_id     bigint               not null
        primary key,
    name            varchar(255)         not null,
    language        varchar(50)          not null,
    template_type   varchar(50)          not null comment '模板类型',
    filename_suffix varchar(255)         null comment '文件名后缀',
    remark          varchar(255)         null,
    template        text                 not null comment '模板内容',
    create_user_id  bigint               null,
    create_time     datetime             not null,
    update_user_id  bigint               null,
    update_time     datetime             null,
    delete_yn       tinyint(1) default 0 not null,
    seq             int                  null comment '序号',
    group_id        bigint               not null,
    constraint uni_db_code_template_name
        unique (name)
)
    comment '代码生成模板表';


INSERT INTO db_code_template (template_id, name, language, filename_suffix, remark, template, create_user_id, create_time, update_user_id, update_time, delete_yn, seq, template_type, group_id) VALUES (1427494473302050, 'smart-boot 实体类', 'text/x-java', 'PO', 'smart-boot框架实体类', 'package ${packages}.model;

<#if mainTable.hasId>
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
</#if>
import com.baomidou.mybatisplus.annotation.TableName;
<#list mainTable.modelClassImportList as item>
import ${item};
</#list>

import com.smart.crud.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

/**
* ${mainTable.tableName} - ${(description)!''''}
* @author SmartCodeGenerator
* ${.now}
*/
@Getter
@Setter
@TableName("${mainTable.tableName}")
public class ${className}PO extends BaseModel {

<#list mainTable.codePageConfigList as item>
    /**
    * ${item.columnName} - ${(item.title)!''''}
    */
    <#if item.idAnnotation>
    @TableId(type = IdType.ASSIGN_ID)
    </#if>
    private ${item.simpleJavaType} ${item.javaProperty};

</#list>
}
', 1, sysdate(), null, null, 0, null, 'template_code', 1);
INSERT INTO db_code_template (template_id, name, language, filename_suffix, remark, template, create_user_id, create_time, update_user_id, update_time, delete_yn, seq, template_type, group_id) VALUES (1427494687211554, 'smart-boot Mapper', 'text/x-java', 'Mapper', 'smart-boot框架 Mapper层', 'package ${packages}.mapper;

import com.smart.crud.mapper.CrudBaseMapper;
import ${packages}.model.${className}PO;

/**
* ${mainTable.tableName} - ${(description)!''''} mapper层
* @author SmartCodeGenerator
* ${.now}
*/
public interface ${className}Mapper extends CrudBaseMapper<${className}PO> {
}
', 1, sysdate(), null, null, 0, null, 'template_code', 1);
INSERT INTO db_code_template (template_id, name, language, filename_suffix, remark, template, create_user_id, create_time, update_user_id, update_time, delete_yn, seq, template_type, group_id) VALUES (1427494997590050, 'smart-boot Mapper XML', 'xml', 'Mapper', '', '<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packages}.mapper.${className}Mapper">
    <sql id="columnList">
    <#list mainTable.codePageConfigList as item>
        <!--   ${(item.title)!''''} <#if item.primaryKey>主键</#if>  <#if item.indexed>索引</#if>   -->
        ${item.columnName},
    </#list>
    </sql>

    <resultMap id="commonResultMap" type="${packages}.model.${className}PO">
    <#list mainTable.codePageConfigList as item>
    <#if item.primaryKey>
        <!--   ${(item.title)!''''} <#if item.primaryKey>主键</#if>  <#if item.indexed>索引</#if>   -->
        <id column="${item.columnName}" property="${item.javaProperty}" javaType="${item.javaType}" jdbcType="${item.typeName}"/>
    <#else >
        <!--   ${(item.title)!''''} <#if item.primaryKey>主键</#if>  <#if item.indexed>索引</#if>   -->
        <result column="${item.columnName}" property="${item.javaProperty}" javaType="${item.javaType}" jdbcType="${item.typeName}"/>
    </#if>
    </#list>
    </resultMap>
</mapper>
', 1, sysdate(), null, null, 0, null, 'template_code', 1);
INSERT INTO db_code_template (template_id, name, language, filename_suffix, remark, template, create_user_id, create_time, update_user_id, update_time, delete_yn, seq, template_type, group_id) VALUES (1427495144390690, 'smart-boot Service', 'text/x-java', 'Service', 'smart-boot框架 Service层', 'package ${packages}.service;

import com.smart.crud.service.BaseService;
import ${packages}.model.${className}PO;

/**
* ${mainTable.tableName} - ${(description)!''''} Service
* @author SmartCodeGenerator
* ${.now}
*/
public interface ${className}Service extends BaseService<${className}PO> {

}
', 1, sysdate(), null, null, 0, null, 'template_code', 1);
INSERT INTO db_code_template (template_id, name, language, filename_suffix, remark, template, create_user_id, create_time, update_user_id, update_time, delete_yn, seq, template_type, group_id) VALUES (1427495406534690, 'smart-boot Service 实现类', 'text/x-java', 'ServiceImpl', 'gc-support框架 Service实现类', 'package ${packages}.service.impl;

import com.smart.crud.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import ${packages}.model.${className}PO;
import ${packages}.service.${className}Service;
import ${packages}.mapper.${className}Mapper;

/**
* ${mainTable.tableName} - ${(description)!''''} Service实现类
* @author SmartCodeGenerator
* ${.now}
*/
@Service
public class ${className}ServiceImpl extends BaseServiceImpl<${className}Mapper, ${className}PO> implements ${className}Service {

}
', 1, sysdate(), null, null, 0, null, 'template_code', 1);
INSERT INTO db_code_template (template_id, name, language, filename_suffix, remark, template, create_user_id, create_time, update_user_id, update_time, delete_yn, seq, template_type, group_id) VALUES (1427495656095778, 'smart-boot Controller', 'text/x-java', 'Controller', 'smart-boot框架 Controller层', 'package ${packages}.controller;

import com.smart.base.message.Result;
import ${packages}.model.${className}PO;
import ${packages}.service.${className}Service;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.collections.CollectionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.smart.commons.core.log.annotation.Log;
import com.smart.commons.core.log.constants.LogOperationTypeEnum;

import java.io.Serializable;
import java.util.List;

/**
* ${mainTable.tableName} - ${(description)!''''} Controller
* @author SmartCodeGenerator
* ${.now}
*/
@RestController
@Tage(name = "${mainTable.tableName} - ${(description)!''''} Controller")
@RequestMapping("${controllerBasePath}")
public class ${className}Controller extends BaseController<${className}Service, ${className}PO> {

    @Override
    @PostMapping("save")
    @Operation(summary = "添加${(description)!''''}")
    @Log(value = "添加${(description)!''''}", type = LogOperationTypeEnum.ADD)
    public Result<Boolean> save(@RequestBody ${className}PO model) {
        return Result.success(this.service.save(model));
    }

    @Override
    @PostMapping("update")
    @Operation(summary = "更新${(description)!''''}")
    @Log(value = "更新${(description)!''''}", type = LogOperationTypeEnum.UPDATE)
    public Result<Boolean> update(@RequestBody ${className}PO model) {
        return super.update(model);
    }

    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Override
    @Operation(summary = "添加修改${(description)!''''}")
    @PostMapping("saveUpdate")
    @Log(value = "添加修改${(description)!''''}", type = LogOperationTypeEnum.UPDATE)
    public Result<Boolean> saveUpdate(@RequestBody ${className}PO model) {
        return super.saveUpdate(model);
    }

    @Override
    @Operation(summary = "通过ID批量删除${(description)!''''}")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除${(description)!''''}", type = LogOperationTypeEnum.DELETE)
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    public Result<${className}PO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }
}
', 1, sysdate(), null, null, 0, null, 'template_code', 1);
INSERT INTO db_code_template (template_id, name, language, filename_suffix, remark, template, create_user_id, create_time, update_user_id, update_time, delete_yn, seq, template_type, group_id) VALUES (1427640453955618, 'smart-boot前台table VUE3', 'javascript', 'ListView', null, '<template>
  <div class="full-height" style="padding: 10px">
    <vxe-grid
      ref="gridRef"
      v-bind="tableProps"
      :size="tableSizeConfig"
      border
      :toolbar-config="toolbarConfig"
      :columns="columns"
      height="auto"
      stripe
      highlight-hover-row>
<#if mainTable.page>
      <template #pager>
        <vxe-pager
          v-bind="pageProps"
          :page-sizes="[500, 1000, 2000, 5000]"
          :layouts="[''Sizes'', ''PrevJump'', ''PrevPage'', ''Number'', ''NextPage'', ''NextJump'', ''FullJump'', ''Total'']" />
      </template>
</#if>
      <#if (mainTable.rightButtonList?size>0)>
      <template #toolbar_tools>
        <a-form layout="inline">
          <a-form-item>
            <#list mainTable.rightButtonList as item>
            <a-button
              :size="buttonSizeConfig"
              type="primary"
              <#if item="ADD">
              @click="() => handleAddEdit(true, null)"
              <#elseif item="EDIT">
              @click="handleEditByCheckbox"
              <#elseif item="DELETE">
              danger
              @click="handleDeleteByCheckbox"
              </#if>
              style="margin-left: 5px">
              <#if item="ADD">
              {{ $t(''common.button.add'') }}
              <#elseif item="EDIT">
              {{ $t(''common.button.edit'') }}
              <#elseif item="DELETE">
              {{ $t(''common.button.delete'') }}
              </#if>
            </a-button>
            </#list>
          </a-form-item>
        </a-form>
      </template>
      </#if>
      <#if (mainTable.leftButtonList?size>0)>
      <template #toolbar_buttons>
        <a-form layout="inline" style="margin-left: 10px">
          <#if (mainTable.searchColNum = 1)>
          <#list mainTable.codeSearchConfigList as item>
          <a-form-item label="${item.title}">
            <#if (item.controlType=''INPUT'')>
            <a-input v-model:value="searchModel.${item.javaProperty}" :size="formSizeConfig" placeholder="请输入${item.title}" />
            <#elseif (item.controlType=''NUMBER'')>
            <a-input-number v-model:value="searchModel.${item.javaProperty}" style="width: 100%" :size="formSizeConfig" placeholder="请输入${item.title}" />
            <#elseif (item.controlType=''TEXTAREA'')>
            <a-textarea v-model:value="searchModel.${item.javaProperty}" :size="formSizeConfig" placeholder="请输入${item.title}" />
            <#elseif (item.controlType=''PASSWORD'')>
            <a-input-password v-model:value="searchModel.${item.javaProperty}" :size="formSizeConfig" placeholder="请输入${item.title}" />
            <#elseif (item.controlType=''RADIO'')>
            <a-radio-group v-model:value="searchModel.${item.javaProperty}" name="radioGroup-${item.javaProperty}">
              // TODO: 待开发
            </a-radio-group>
            </#if>
          </a-form-item>
          </#list>
          </#if>
          <a-form-item>
            <#list mainTable.leftButtonList as item>
            <a-button
              :size="buttonSizeConfig"
              <#if item="SEARCH">
              type="primary"
              @click="loadData"
              <#elseif item="RESET">
              @click="handleReset"
              </#if>
              style="margin-left: 5px">
              <#if item="SEARCH">
              {{ $t(''common.button.search'') }}
              <#elseif item="RESET">
              {{ $t(''common.button.reset'') }}
              </#if>
            </a-button>
            </#list>
          </a-form-item>
        </a-form>
      </template>
      </#if>
      <#if (mainTable.rowButtonList?size>0)>
      <template #table-operation="{ row }">
        <#if (mainTable.rowButtonType="MORE")>
        <a-dropdown>
          <a-button :size="tableButtonSizeConfig" type="primary">
            Actions
            <DownOutlined />
          </a-button>
          <template #overlay>
            <a-menu @click="({ key }) => handleActions(row, key)">
              <#list mainTable.rowButtonList as item>
              <a-menu-item key="${item}">
                <#if (item = "EDIT")>
                {{ $t(''common.button.edit'') }}
                </#if>
                <#if (item = "DELETE")>
                {{ $t(''common.button.delete'') }}
                </#if>
                <#if (item = "ADD")>
                {{ $t(''common.button.add'') }}
                </#if>
              </a-menu-item>
              </#list>
            </a-menu>
          </template>
        </a-dropdown>
        </#if>
      </template>
      </#if>
    </vxe-grid>
    <#if (mainTable.codeFormConfigList?size>0)>
    <a-modal
      v-bind="modalProps">
      <a-spin :spinning="spinning">
        <a-form
          ref="formRef"
          :rules="rules"
          :label-col="{span: 5}"
          :wrapper-col="{span: 18}"
          v-bind="formProps">
          <a-row>
            <#list mainTable.codeFormConfigList as item>
            <#if item.hidden>
            <a-input v-model:value="formProps.model.${item.javaProperty}"/>
             <#else>
            <a-col :span="${24/mainTable.formColNum}">
              <a-form-item label="${item.title}" name="${item.javaProperty}">
                <#if (item.controlType=''INPUT'')>
                <a-input v-model:value="formProps.model.${item.javaProperty}" :size="formSizeConfig" placeholder="请输入${item.title}" />
                <#elseif (item.controlType=''NUMBER'')>
                <a-input-number v-model:value="formProps.model.${item.javaProperty}" style="width: 100%" :size="formSizeConfig" placeholder="请输入${item.title}" />
                <#elseif (item.controlType=''TEXTAREA'')>
                <a-textarea v-model:value="formProps.model.${item.javaProperty}" :size="formSizeConfig" placeholder="请输入${item.title}" />
                <#elseif (item.controlType=''PASSWORD'')>
                <a-input-password v-model:value="formProps.model.${item.javaProperty}" :size="formSizeConfig" placeholder="请输入${item.title}" />
                <#elseif (item.controlType=''RADIO'')>
                <a-radio-group v-model:value="formProps.model.${item.javaProperty}" name="radioGroup-${item.javaProperty}"></a-radio-group>
                </#if>
              </a-form-item>
            </a-col>
            </#if>
            </#list>
          </a-row>
        </a-form>
      </a-spin>
    </a-modal>
    </#if>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from ''vue''
import { useI18n } from ''vue-i18n''

import { <#if (mainTable.rowButtonType="MORE")>DownOutlined</#if> } from ''@ant-design/icons-vue''

import { useVxeTable<#if (mainTable.codeFormConfigList?size>0)>, useAddEdit</#if>, useVxeDelete } from ''@/components/hooks''
import SizeConfigHooks from ''@/components/config/SizeConfigHooks''

import { handleLoadData<#if (mainTable.codeFormConfigList?size>0)>, handleGetById, handleSaveUpdate</#if>, handleDelete } from ''./${className}Hook''

export default defineComponent({
  name: ''${className}ListView'',
  components: {
    <#if (mainTable.rowButtonType="MORE")>DownOutlined</#if>
  },
  setup () {
    const { t } = useI18n()
    const gridRef = ref()

    /**
     * 查询数据hook
     */
    const { tableProps, handleReset<#if mainTable.page>, pageProps</#if><#if (mainTable.codeFormConfigList?size>0)>, searchModel</#if>, loadData } = useVxeTable(handleLoadData, {
      paging: ${mainTable.page?string("true", "false")}
    })
    <#if (mainTable.codeFormConfigList?size>0)>

    /**
     * 添加保存hook
     */
    const addEditHook = useAddEdit(gridRef, handleGetById, loadData, handleSaveUpdate, t, {
      idField: ''${mainTable.idField.javaProperty}''
    })
    </#if>

    /**
     * 删除hook
     */
    const deleteHook = useVxeDelete(gridRef, t, handleDelete, { idField: ''${mainTable.idField.javaProperty}'', listHandler: loadData })

    <#if (mainTable.rowButtonType="MORE")>
    /**
     * 按钮操作
     * @param row 行数据
     * @param action 操作
     */
    const handleActions = (row: any, action: String) => {
      switch (action) {
        <#list mainTable.rowButtonList as item>
        case ''${item}'': {
          <#if (item = "EDIT")>
          addEditHook.handleAddEdit(false, row.${mainTable.idField.javaProperty})
          <#elseif (item = "DELETE")>
          deleteHook.handleDeleteByRow(row)
          </#if>
          break
        }
        </#list>
      }
    }
    </#if>
    onMounted(loadData)
    return {
      gridRef,
      ...SizeConfigHooks(),
      <#if (mainTable.codeFormConfigList?size>0)>
      ...addEditHook,
      </#if>
      <#if (mainTable.codeFormConfigList?size>0)>
      searchModel,
      </#if>
      ...deleteHook,
      tableProps,
      <#if mainTable.page>
      pageProps,
      </#if>
      loadData,
      handleReset,
      handleActions
    }
  },
  data () {
    return {
      toolbarConfig: {
        slots: {
          <#if (mainTable.rightButtonList?size>0)>
          tools: ''toolbar_tools'',
          </#if>
          <#if (mainTable.leftButtonList?size>0)>
          buttons: ''toolbar_buttons''
          </#if>
        }
      },
      <#if (mainTable.codeFormConfigList?size>0)>
      rules: {
        <#list mainTable.codeFormConfigList as item>
        <#if (item.ruleList?size>0) >
        ${item.javaProperty}: [
          <#list item.ruleList as rule>
          {
            required: true,
            trigger: [
              <#list rule.ruleTrigger as trigger>
              ''${trigger}''
              </#list>
            ],
            <#if rule.ruleType=''NUMBER''>
            type: ''number'',
            </#if>
            message: ''${rule.message}''
          },
          </#list>
        ]
        </#if>
        </#list>
      },
      </#if>
      columns: [
       <#if mainTable.showCheckbox>
        {
          type: ''checkbox'',
          width: 60,
          align: ''center'',
          fixed: ''left''
        },
       </#if>
       <#list mainTable.codePageConfigList as item>
        {
          title: ''${item.title}'',
          field: ''${item.javaProperty}'',
          <#if item.fixed??>
          fixed: ''${item.fixed}'',
          </#if>
          <#if item.hidden>
          hidden: true,
          </#if>
          <#if item.sortable>
          sortable: true,
          </#if>
          width: ${item.width}
        },
       </#list>
        <#if (mainTable.rowButtonList?size>0)>
        {
          title: ''{common.table.operation}'',
          field: ''operation'',
          width: 120,
          fixed: ''right'',
          slots: {
            default: ''table-operation''
          }
        }
        </#if>
      ]
    }
  }
})
</script>
', 1, sysdate(), null, null, 0, null, 'template_code', 1);
INSERT INTO db_code_template (template_id, name, language, filename_suffix, remark, template, create_user_id, create_time, update_user_id, update_time, delete_yn, seq, template_type, group_id) VALUES (1427640525258786, 'support 前台support VUE3', 'javascript', 'ListSupport', null, 'import ApiService from ''@/common/utils/ApiService''
import { errorMessage } from ''@/components/notice/SystemNotice''

/**
 * 加载数据
 * @param params 参数
 * @param searchParameter 查询参数
 */
export const handleLoadData = async (params: any, searchParameter: any) => {
  try {
  	return await ApiService.postAjax(''${controllerBasePath}list'', {
	  ...params,
	  parameter: searchParameter
  	})
  } catch (e) {
		errorMessage(e)
		throw e
  }
}


<#if (mainTable.codeFormConfigList?size>0)>
/**
 * 通过ID查询
 * @param id ID
 */
export const handleGetById = async (id: number) => {
	try {
		return await ApiService.postAjax(''${controllerBasePath}getById'', id)
	} catch (e) {
		errorMessage(e)
		throw e
	}
}

/**
 * 添加保存函数
 * @param model 添加保存参数
 */
export const handleSaveUpdate = async (model: any) => {
	try {
		await ApiService.postAjax(''${controllerBasePath}saveUpdate'', model)
	} catch (e) {
		errorMessage(e)
		throw e
	}
}
</#if>


/**
 * 删除函数
 * @param idList ID列表
 */
export const handleDelete = async (idList: Array<any>) => {
	try {
		await ApiService.postAjax(''${controllerBasePath}batchDeleteById'', idList)
	} catch (e) {
		errorMessage(e)
		throw e
	}
}
', 1, sysdate(), null, null, 0, null, 'template_code', 1);
INSERT INTO db_code_template (template_id, name, language, filename_suffix, remark, template, create_user_id, create_time, update_user_id, update_time, delete_yn, seq, template_type, group_id) VALUES (1428027951022114, '数据库字典模板（Word）', 'html', '', null, '<html xmlns:v="urn:schemas-microsoft-com:vml"
xmlns:o="urn:schemas-microsoft-com:office:office"
xmlns:w="urn:schemas-microsoft-com:office:word"
xmlns:x="urn:schemas-microsoft-com:office:excel"
xmlns:m="http://schemas.microsoft.com/office/2004/12/omml"
xmlns="http://www.w3.org/TR/REC-html40">

<head>
<meta http-equiv=Content-Type content="text/html; charset=unicode">
<meta name=ProgId content=Word.Document>
<meta name=Generator content="Microsoft Word 15">
<meta name=Originator content="Microsoft Word 15">
<link rel=File-List href="databaseDic.fld/filelist.xml">
<!--[if gte mso 9]><xml>
 <o:DocumentProperties>
  <o:Author>仲 明</o:Author>
  <o:LastAuthor>Shi, ZhongMing (CW)</o:LastAuthor>
  <o:Revision>12</o:Revision>
  <o:TotalTime>60</o:TotalTime>
  <o:Created>2020-07-03T01:49:00Z</o:Created>
  <o:LastSaved>2020-08-25T06:58:00Z</o:LastSaved>
  <o:Pages>2</o:Pages>
  <o:Words>24</o:Words>
  <o:Characters>142</o:Characters>
  <o:Lines>1</o:Lines>
  <o:Paragraphs>1</o:Paragraphs>
  <o:CharactersWithSpaces>165</o:CharactersWithSpaces>
  <o:Version>16.00</o:Version>
 </o:DocumentProperties>
 <o:OfficeDocumentSettings>
  <o:AllowPNG/>
 </o:OfficeDocumentSettings>
</xml><![endif]-->
<link rel=themeData href="databaseDic.fld/themedata.thmx">
<link rel=colorSchemeMapping href="databaseDic.fld/colorschememapping.xml">
<!--[if gte mso 9]><xml>
 <w:WordDocument>
  <w:View>Print</w:View>
  <w:Zoom>110</w:Zoom>
  <w:SpellingState>Clean</w:SpellingState>
  <w:GrammarState>Clean</w:GrammarState>
  <w:TrackMoves>false</w:TrackMoves>
  <w:TrackFormatting/>
  <w:PunctuationKerning/>
  <w:DrawingGridVerticalSpacing>7.8 磅</w:DrawingGridVerticalSpacing>
  <w:DisplayHorizontalDrawingGridEvery>0</w:DisplayHorizontalDrawingGridEvery>
  <w:DisplayVerticalDrawingGridEvery>2</w:DisplayVerticalDrawingGridEvery>
  <w:ValidateAgainstSchemas/>
  <w:SaveIfXMLInvalid>false</w:SaveIfXMLInvalid>
  <w:IgnoreMixedContent>false</w:IgnoreMixedContent>
  <w:AlwaysShowPlaceholderText>false</w:AlwaysShowPlaceholderText>
  <w:DoNotPromoteQF/>
  <w:LidThemeOther>EN-US</w:LidThemeOther>
  <w:LidThemeAsian>ZH-CN</w:LidThemeAsian>
  <w:LidThemeComplexScript>X-NONE</w:LidThemeComplexScript>
  <w:Compatibility>
   <w:SpaceForUL/>
   <w:BalanceSingleByteDoubleByteWidth/>
   <w:DoNotLeaveBackslashAlone/>
   <w:ULTrailSpace/>
   <w:DoNotExpandShiftReturn/>
   <w:AdjustLineHeightInTable/>
   <w:BreakWrappedTables/>
   <w:SnapToGridInCell/>
   <w:WrapTextWithPunct/>
   <w:UseAsianBreakRules/>
   <w:DontGrowAutofit/>
   <w:SplitPgBreakAndParaMark/>
   <w:EnableOpenTypeKerning/>
   <w:DontFlipMirrorIndents/>
   <w:OverrideTableStyleHps/>
   <w:UseFELayout/>
  </w:Compatibility>
  <w:BrowserLevel>MicrosoftInternetExplorer4</w:BrowserLevel>
  <m:mathPr>
   <m:mathFont m:val="Cambria Math"/>
   <m:brkBin m:val="before"/>
   <m:brkBinSub m:val="&#45;-"/>
   <m:smallFrac m:val="off"/>
   <m:dispDef/>
   <m:lMargin m:val="0"/>
   <m:rMargin m:val="0"/>
   <m:defJc m:val="centerGroup"/>
   <m:wrapIndent m:val="1440"/>
   <m:intLim m:val="subSup"/>
   <m:naryLim m:val="undOvr"/>
  </m:mathPr></w:WordDocument>
</xml><![endif]--><!--[if gte mso 9]><xml>
 <w:LatentStyles DefLockedState="false" DefUnhideWhenUsed="false"
  DefSemiHidden="false" DefQFormat="false" DefPriority="99"
  LatentStyleCount="376">
  <w:LsdException Locked="false" Priority="0" QFormat="true" Name="Normal"/>
  <w:LsdException Locked="false" Priority="9" QFormat="true" Name="heading 1"/>
  <w:LsdException Locked="false" Priority="9" SemiHidden="true"
   UnhideWhenUsed="true" QFormat="true" Name="heading 2"/>
  <w:LsdException Locked="false" Priority="9" SemiHidden="true"
   UnhideWhenUsed="true" QFormat="true" Name="heading 3"/>
  <w:LsdException Locked="false" Priority="9" SemiHidden="true"
   UnhideWhenUsed="true" QFormat="true" Name="heading 4"/>
  <w:LsdException Locked="false" Priority="9" SemiHidden="true"
   UnhideWhenUsed="true" QFormat="true" Name="heading 5"/>
  <w:LsdException Locked="false" Priority="9" SemiHidden="true"
   UnhideWhenUsed="true" QFormat="true" Name="heading 6"/>
  <w:LsdException Locked="false" Priority="9" SemiHidden="true"
   UnhideWhenUsed="true" QFormat="true" Name="heading 7"/>
  <w:LsdException Locked="false" Priority="9" SemiHidden="true"
   UnhideWhenUsed="true" QFormat="true" Name="heading 8"/>
  <w:LsdException Locked="false" Priority="9" SemiHidden="true"
   UnhideWhenUsed="true" QFormat="true" Name="heading 9"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="index 1"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="index 2"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="index 3"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="index 4"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="index 5"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="index 6"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="index 7"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="index 8"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="index 9"/>
  <w:LsdException Locked="false" Priority="39" SemiHidden="true"
   UnhideWhenUsed="true" Name="toc 1"/>
  <w:LsdException Locked="false" Priority="39" SemiHidden="true"
   UnhideWhenUsed="true" Name="toc 2"/>
  <w:LsdException Locked="false" Priority="39" SemiHidden="true"
   UnhideWhenUsed="true" Name="toc 3"/>
  <w:LsdException Locked="false" Priority="39" SemiHidden="true"
   UnhideWhenUsed="true" Name="toc 4"/>
  <w:LsdException Locked="false" Priority="39" SemiHidden="true"
   UnhideWhenUsed="true" Name="toc 5"/>
  <w:LsdException Locked="false" Priority="39" SemiHidden="true"
   UnhideWhenUsed="true" Name="toc 6"/>
  <w:LsdException Locked="false" Priority="39" SemiHidden="true"
   UnhideWhenUsed="true" Name="toc 7"/>
  <w:LsdException Locked="false" Priority="39" SemiHidden="true"
   UnhideWhenUsed="true" Name="toc 8"/>
  <w:LsdException Locked="false" Priority="39" SemiHidden="true"
   UnhideWhenUsed="true" Name="toc 9"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Normal Indent"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="footnote text"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="annotation text"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="header"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="footer"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="index heading"/>
  <w:LsdException Locked="false" Priority="35" SemiHidden="true"
   UnhideWhenUsed="true" QFormat="true" Name="caption"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="table of figures"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="envelope address"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="envelope return"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="footnote reference"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="annotation reference"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="line number"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="page number"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="endnote reference"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="endnote text"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="table of authorities"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="macro"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="toa heading"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="List"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="List Bullet"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="List Number"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="List 2"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="List 3"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="List 4"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="List 5"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="List Bullet 2"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="List Bullet 3"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="List Bullet 4"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="List Bullet 5"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="List Number 2"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="List Number 3"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="List Number 4"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="List Number 5"/>
  <w:LsdException Locked="false" Priority="10" QFormat="true" Name="Title"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Closing"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Signature"/>
  <w:LsdException Locked="false" Priority="1" SemiHidden="true"
   UnhideWhenUsed="true" Name="Default Paragraph Font"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Body Text"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Body Text Indent"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="List Continue"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="List Continue 2"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="List Continue 3"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="List Continue 4"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="List Continue 5"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Message Header"/>
  <w:LsdException Locked="false" Priority="11" QFormat="true" Name="Subtitle"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Salutation"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Date"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Body Text First Indent"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Body Text First Indent 2"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Note Heading"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Body Text 2"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Body Text 3"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Body Text Indent 2"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Body Text Indent 3"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Block Text"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Hyperlink"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="FollowedHyperlink"/>
  <w:LsdException Locked="false" Priority="22" QFormat="true" Name="Strong"/>
  <w:LsdException Locked="false" Priority="20" QFormat="true" Name="Emphasis"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Document Map"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Plain Text"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="E-mail Signature"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="HTML Top of Form"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="HTML Bottom of Form"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Normal (Web)"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="HTML Acronym"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="HTML Address"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="HTML Cite"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="HTML Code"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="HTML Definition"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="HTML Keyboard"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="HTML Preformatted"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="HTML Sample"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="HTML Typewriter"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="HTML Variable"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Normal Table"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="annotation subject"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="No List"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Outline List 1"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Outline List 2"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Outline List 3"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Simple 1"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Simple 2"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Simple 3"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Classic 1"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Classic 2"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Classic 3"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Classic 4"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Colorful 1"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Colorful 2"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Colorful 3"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Columns 1"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Columns 2"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Columns 3"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Columns 4"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Columns 5"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Grid 1"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Grid 2"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Grid 3"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Grid 4"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Grid 5"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Grid 6"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Grid 7"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Grid 8"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table List 1"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table List 2"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table List 3"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table List 4"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table List 5"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table List 6"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table List 7"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table List 8"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table 3D effects 1"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table 3D effects 2"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table 3D effects 3"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Contemporary"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Elegant"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Professional"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Subtle 1"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Subtle 2"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Web 1"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Web 2"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Web 3"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Balloon Text"/>
  <w:LsdException Locked="false" Priority="39" Name="Table Grid"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Table Theme"/>
  <w:LsdException Locked="false" SemiHidden="true" Name="Placeholder Text"/>
  <w:LsdException Locked="false" Priority="1" QFormat="true" Name="No Spacing"/>
  <w:LsdException Locked="false" Priority="60" Name="Light Shading"/>
  <w:LsdException Locked="false" Priority="61" Name="Light List"/>
  <w:LsdException Locked="false" Priority="62" Name="Light Grid"/>
  <w:LsdException Locked="false" Priority="63" Name="Medium Shading 1"/>
  <w:LsdException Locked="false" Priority="64" Name="Medium Shading 2"/>
  <w:LsdException Locked="false" Priority="65" Name="Medium List 1"/>
  <w:LsdException Locked="false" Priority="66" Name="Medium List 2"/>
  <w:LsdException Locked="false" Priority="67" Name="Medium Grid 1"/>
  <w:LsdException Locked="false" Priority="68" Name="Medium Grid 2"/>
  <w:LsdException Locked="false" Priority="69" Name="Medium Grid 3"/>
  <w:LsdException Locked="false" Priority="70" Name="Dark List"/>
  <w:LsdException Locked="false" Priority="71" Name="Colorful Shading"/>
  <w:LsdException Locked="false" Priority="72" Name="Colorful List"/>
  <w:LsdException Locked="false" Priority="73" Name="Colorful Grid"/>
  <w:LsdException Locked="false" Priority="60" Name="Light Shading Accent 1"/>
  <w:LsdException Locked="false" Priority="61" Name="Light List Accent 1"/>
  <w:LsdException Locked="false" Priority="62" Name="Light Grid Accent 1"/>
  <w:LsdException Locked="false" Priority="63" Name="Medium Shading 1 Accent 1"/>
  <w:LsdException Locked="false" Priority="64" Name="Medium Shading 2 Accent 1"/>
  <w:LsdException Locked="false" Priority="65" Name="Medium List 1 Accent 1"/>
  <w:LsdException Locked="false" SemiHidden="true" Name="Revision"/>
  <w:LsdException Locked="false" Priority="34" QFormat="true"
   Name="List Paragraph"/>
  <w:LsdException Locked="false" Priority="29" QFormat="true" Name="Quote"/>
  <w:LsdException Locked="false" Priority="30" QFormat="true"
   Name="Intense Quote"/>
  <w:LsdException Locked="false" Priority="66" Name="Medium List 2 Accent 1"/>
  <w:LsdException Locked="false" Priority="67" Name="Medium Grid 1 Accent 1"/>
  <w:LsdException Locked="false" Priority="68" Name="Medium Grid 2 Accent 1"/>
  <w:LsdException Locked="false" Priority="69" Name="Medium Grid 3 Accent 1"/>
  <w:LsdException Locked="false" Priority="70" Name="Dark List Accent 1"/>
  <w:LsdException Locked="false" Priority="71" Name="Colorful Shading Accent 1"/>
  <w:LsdException Locked="false" Priority="72" Name="Colorful List Accent 1"/>
  <w:LsdException Locked="false" Priority="73" Name="Colorful Grid Accent 1"/>
  <w:LsdException Locked="false" Priority="60" Name="Light Shading Accent 2"/>
  <w:LsdException Locked="false" Priority="61" Name="Light List Accent 2"/>
  <w:LsdException Locked="false" Priority="62" Name="Light Grid Accent 2"/>
  <w:LsdException Locked="false" Priority="63" Name="Medium Shading 1 Accent 2"/>
  <w:LsdException Locked="false" Priority="64" Name="Medium Shading 2 Accent 2"/>
  <w:LsdException Locked="false" Priority="65" Name="Medium List 1 Accent 2"/>
  <w:LsdException Locked="false" Priority="66" Name="Medium List 2 Accent 2"/>
  <w:LsdException Locked="false" Priority="67" Name="Medium Grid 1 Accent 2"/>
  <w:LsdException Locked="false" Priority="68" Name="Medium Grid 2 Accent 2"/>
  <w:LsdException Locked="false" Priority="69" Name="Medium Grid 3 Accent 2"/>
  <w:LsdException Locked="false" Priority="70" Name="Dark List Accent 2"/>
  <w:LsdException Locked="false" Priority="71" Name="Colorful Shading Accent 2"/>
  <w:LsdException Locked="false" Priority="72" Name="Colorful List Accent 2"/>
  <w:LsdException Locked="false" Priority="73" Name="Colorful Grid Accent 2"/>
  <w:LsdException Locked="false" Priority="60" Name="Light Shading Accent 3"/>
  <w:LsdException Locked="false" Priority="61" Name="Light List Accent 3"/>
  <w:LsdException Locked="false" Priority="62" Name="Light Grid Accent 3"/>
  <w:LsdException Locked="false" Priority="63" Name="Medium Shading 1 Accent 3"/>
  <w:LsdException Locked="false" Priority="64" Name="Medium Shading 2 Accent 3"/>
  <w:LsdException Locked="false" Priority="65" Name="Medium List 1 Accent 3"/>
  <w:LsdException Locked="false" Priority="66" Name="Medium List 2 Accent 3"/>
  <w:LsdException Locked="false" Priority="67" Name="Medium Grid 1 Accent 3"/>
  <w:LsdException Locked="false" Priority="68" Name="Medium Grid 2 Accent 3"/>
  <w:LsdException Locked="false" Priority="69" Name="Medium Grid 3 Accent 3"/>
  <w:LsdException Locked="false" Priority="70" Name="Dark List Accent 3"/>
  <w:LsdException Locked="false" Priority="71" Name="Colorful Shading Accent 3"/>
  <w:LsdException Locked="false" Priority="72" Name="Colorful List Accent 3"/>
  <w:LsdException Locked="false" Priority="73" Name="Colorful Grid Accent 3"/>
  <w:LsdException Locked="false" Priority="60" Name="Light Shading Accent 4"/>
  <w:LsdException Locked="false" Priority="61" Name="Light List Accent 4"/>
  <w:LsdException Locked="false" Priority="62" Name="Light Grid Accent 4"/>
  <w:LsdException Locked="false" Priority="63" Name="Medium Shading 1 Accent 4"/>
  <w:LsdException Locked="false" Priority="64" Name="Medium Shading 2 Accent 4"/>
  <w:LsdException Locked="false" Priority="65" Name="Medium List 1 Accent 4"/>
  <w:LsdException Locked="false" Priority="66" Name="Medium List 2 Accent 4"/>
  <w:LsdException Locked="false" Priority="67" Name="Medium Grid 1 Accent 4"/>
  <w:LsdException Locked="false" Priority="68" Name="Medium Grid 2 Accent 4"/>
  <w:LsdException Locked="false" Priority="69" Name="Medium Grid 3 Accent 4"/>
  <w:LsdException Locked="false" Priority="70" Name="Dark List Accent 4"/>
  <w:LsdException Locked="false" Priority="71" Name="Colorful Shading Accent 4"/>
  <w:LsdException Locked="false" Priority="72" Name="Colorful List Accent 4"/>
  <w:LsdException Locked="false" Priority="73" Name="Colorful Grid Accent 4"/>
  <w:LsdException Locked="false" Priority="60" Name="Light Shading Accent 5"/>
  <w:LsdException Locked="false" Priority="61" Name="Light List Accent 5"/>
  <w:LsdException Locked="false" Priority="62" Name="Light Grid Accent 5"/>
  <w:LsdException Locked="false" Priority="63" Name="Medium Shading 1 Accent 5"/>
  <w:LsdException Locked="false" Priority="64" Name="Medium Shading 2 Accent 5"/>
  <w:LsdException Locked="false" Priority="65" Name="Medium List 1 Accent 5"/>
  <w:LsdException Locked="false" Priority="66" Name="Medium List 2 Accent 5"/>
  <w:LsdException Locked="false" Priority="67" Name="Medium Grid 1 Accent 5"/>
  <w:LsdException Locked="false" Priority="68" Name="Medium Grid 2 Accent 5"/>
  <w:LsdException Locked="false" Priority="69" Name="Medium Grid 3 Accent 5"/>
  <w:LsdException Locked="false" Priority="70" Name="Dark List Accent 5"/>
  <w:LsdException Locked="false" Priority="71" Name="Colorful Shading Accent 5"/>
  <w:LsdException Locked="false" Priority="72" Name="Colorful List Accent 5"/>
  <w:LsdException Locked="false" Priority="73" Name="Colorful Grid Accent 5"/>
  <w:LsdException Locked="false" Priority="60" Name="Light Shading Accent 6"/>
  <w:LsdException Locked="false" Priority="61" Name="Light List Accent 6"/>
  <w:LsdException Locked="false" Priority="62" Name="Light Grid Accent 6"/>
  <w:LsdException Locked="false" Priority="63" Name="Medium Shading 1 Accent 6"/>
  <w:LsdException Locked="false" Priority="64" Name="Medium Shading 2 Accent 6"/>
  <w:LsdException Locked="false" Priority="65" Name="Medium List 1 Accent 6"/>
  <w:LsdException Locked="false" Priority="66" Name="Medium List 2 Accent 6"/>
  <w:LsdException Locked="false" Priority="67" Name="Medium Grid 1 Accent 6"/>
  <w:LsdException Locked="false" Priority="68" Name="Medium Grid 2 Accent 6"/>
  <w:LsdException Locked="false" Priority="69" Name="Medium Grid 3 Accent 6"/>
  <w:LsdException Locked="false" Priority="70" Name="Dark List Accent 6"/>
  <w:LsdException Locked="false" Priority="71" Name="Colorful Shading Accent 6"/>
  <w:LsdException Locked="false" Priority="72" Name="Colorful List Accent 6"/>
  <w:LsdException Locked="false" Priority="73" Name="Colorful Grid Accent 6"/>
  <w:LsdException Locked="false" Priority="19" QFormat="true"
   Name="Subtle Emphasis"/>
  <w:LsdException Locked="false" Priority="21" QFormat="true"
   Name="Intense Emphasis"/>
  <w:LsdException Locked="false" Priority="31" QFormat="true"
   Name="Subtle Reference"/>
  <w:LsdException Locked="false" Priority="32" QFormat="true"
   Name="Intense Reference"/>
  <w:LsdException Locked="false" Priority="33" QFormat="true" Name="Book Title"/>
  <w:LsdException Locked="false" Priority="37" SemiHidden="true"
   UnhideWhenUsed="true" Name="Bibliography"/>
  <w:LsdException Locked="false" Priority="39" SemiHidden="true"
   UnhideWhenUsed="true" QFormat="true" Name="TOC Heading"/>
  <w:LsdException Locked="false" Priority="41" Name="Plain Table 1"/>
  <w:LsdException Locked="false" Priority="42" Name="Plain Table 2"/>
  <w:LsdException Locked="false" Priority="43" Name="Plain Table 3"/>
  <w:LsdException Locked="false" Priority="44" Name="Plain Table 4"/>
  <w:LsdException Locked="false" Priority="45" Name="Plain Table 5"/>
  <w:LsdException Locked="false" Priority="40" Name="Grid Table Light"/>
  <w:LsdException Locked="false" Priority="46" Name="Grid Table 1 Light"/>
  <w:LsdException Locked="false" Priority="47" Name="Grid Table 2"/>
  <w:LsdException Locked="false" Priority="48" Name="Grid Table 3"/>
  <w:LsdException Locked="false" Priority="49" Name="Grid Table 4"/>
  <w:LsdException Locked="false" Priority="50" Name="Grid Table 5 Dark"/>
  <w:LsdException Locked="false" Priority="51" Name="Grid Table 6 Colorful"/>
  <w:LsdException Locked="false" Priority="52" Name="Grid Table 7 Colorful"/>
  <w:LsdException Locked="false" Priority="46"
   Name="Grid Table 1 Light Accent 1"/>
  <w:LsdException Locked="false" Priority="47" Name="Grid Table 2 Accent 1"/>
  <w:LsdException Locked="false" Priority="48" Name="Grid Table 3 Accent 1"/>
  <w:LsdException Locked="false" Priority="49" Name="Grid Table 4 Accent 1"/>
  <w:LsdException Locked="false" Priority="50" Name="Grid Table 5 Dark Accent 1"/>
  <w:LsdException Locked="false" Priority="51"
   Name="Grid Table 6 Colorful Accent 1"/>
  <w:LsdException Locked="false" Priority="52"
   Name="Grid Table 7 Colorful Accent 1"/>
  <w:LsdException Locked="false" Priority="46"
   Name="Grid Table 1 Light Accent 2"/>
  <w:LsdException Locked="false" Priority="47" Name="Grid Table 2 Accent 2"/>
  <w:LsdException Locked="false" Priority="48" Name="Grid Table 3 Accent 2"/>
  <w:LsdException Locked="false" Priority="49" Name="Grid Table 4 Accent 2"/>
  <w:LsdException Locked="false" Priority="50" Name="Grid Table 5 Dark Accent 2"/>
  <w:LsdException Locked="false" Priority="51"
   Name="Grid Table 6 Colorful Accent 2"/>
  <w:LsdException Locked="false" Priority="52"
   Name="Grid Table 7 Colorful Accent 2"/>
  <w:LsdException Locked="false" Priority="46"
   Name="Grid Table 1 Light Accent 3"/>
  <w:LsdException Locked="false" Priority="47" Name="Grid Table 2 Accent 3"/>
  <w:LsdException Locked="false" Priority="48" Name="Grid Table 3 Accent 3"/>
  <w:LsdException Locked="false" Priority="49" Name="Grid Table 4 Accent 3"/>
  <w:LsdException Locked="false" Priority="50" Name="Grid Table 5 Dark Accent 3"/>
  <w:LsdException Locked="false" Priority="51"
   Name="Grid Table 6 Colorful Accent 3"/>
  <w:LsdException Locked="false" Priority="52"
   Name="Grid Table 7 Colorful Accent 3"/>
  <w:LsdException Locked="false" Priority="46"
   Name="Grid Table 1 Light Accent 4"/>
  <w:LsdException Locked="false" Priority="47" Name="Grid Table 2 Accent 4"/>
  <w:LsdException Locked="false" Priority="48" Name="Grid Table 3 Accent 4"/>
  <w:LsdException Locked="false" Priority="49" Name="Grid Table 4 Accent 4"/>
  <w:LsdException Locked="false" Priority="50" Name="Grid Table 5 Dark Accent 4"/>
  <w:LsdException Locked="false" Priority="51"
   Name="Grid Table 6 Colorful Accent 4"/>
  <w:LsdException Locked="false" Priority="52"
   Name="Grid Table 7 Colorful Accent 4"/>
  <w:LsdException Locked="false" Priority="46"
   Name="Grid Table 1 Light Accent 5"/>
  <w:LsdException Locked="false" Priority="47" Name="Grid Table 2 Accent 5"/>
  <w:LsdException Locked="false" Priority="48" Name="Grid Table 3 Accent 5"/>
  <w:LsdException Locked="false" Priority="49" Name="Grid Table 4 Accent 5"/>
  <w:LsdException Locked="false" Priority="50" Name="Grid Table 5 Dark Accent 5"/>
  <w:LsdException Locked="false" Priority="51"
   Name="Grid Table 6 Colorful Accent 5"/>
  <w:LsdException Locked="false" Priority="52"
   Name="Grid Table 7 Colorful Accent 5"/>
  <w:LsdException Locked="false" Priority="46"
   Name="Grid Table 1 Light Accent 6"/>
  <w:LsdException Locked="false" Priority="47" Name="Grid Table 2 Accent 6"/>
  <w:LsdException Locked="false" Priority="48" Name="Grid Table 3 Accent 6"/>
  <w:LsdException Locked="false" Priority="49" Name="Grid Table 4 Accent 6"/>
  <w:LsdException Locked="false" Priority="50" Name="Grid Table 5 Dark Accent 6"/>
  <w:LsdException Locked="false" Priority="51"
   Name="Grid Table 6 Colorful Accent 6"/>
  <w:LsdException Locked="false" Priority="52"
   Name="Grid Table 7 Colorful Accent 6"/>
  <w:LsdException Locked="false" Priority="46" Name="List Table 1 Light"/>
  <w:LsdException Locked="false" Priority="47" Name="List Table 2"/>
  <w:LsdException Locked="false" Priority="48" Name="List Table 3"/>
  <w:LsdException Locked="false" Priority="49" Name="List Table 4"/>
  <w:LsdException Locked="false" Priority="50" Name="List Table 5 Dark"/>
  <w:LsdException Locked="false" Priority="51" Name="List Table 6 Colorful"/>
  <w:LsdException Locked="false" Priority="52" Name="List Table 7 Colorful"/>
  <w:LsdException Locked="false" Priority="46"
   Name="List Table 1 Light Accent 1"/>
  <w:LsdException Locked="false" Priority="47" Name="List Table 2 Accent 1"/>
  <w:LsdException Locked="false" Priority="48" Name="List Table 3 Accent 1"/>
  <w:LsdException Locked="false" Priority="49" Name="List Table 4 Accent 1"/>
  <w:LsdException Locked="false" Priority="50" Name="List Table 5 Dark Accent 1"/>
  <w:LsdException Locked="false" Priority="51"
   Name="List Table 6 Colorful Accent 1"/>
  <w:LsdException Locked="false" Priority="52"
   Name="List Table 7 Colorful Accent 1"/>
  <w:LsdException Locked="false" Priority="46"
   Name="List Table 1 Light Accent 2"/>
  <w:LsdException Locked="false" Priority="47" Name="List Table 2 Accent 2"/>
  <w:LsdException Locked="false" Priority="48" Name="List Table 3 Accent 2"/>
  <w:LsdException Locked="false" Priority="49" Name="List Table 4 Accent 2"/>
  <w:LsdException Locked="false" Priority="50" Name="List Table 5 Dark Accent 2"/>
  <w:LsdException Locked="false" Priority="51"
   Name="List Table 6 Colorful Accent 2"/>
  <w:LsdException Locked="false" Priority="52"
   Name="List Table 7 Colorful Accent 2"/>
  <w:LsdException Locked="false" Priority="46"
   Name="List Table 1 Light Accent 3"/>
  <w:LsdException Locked="false" Priority="47" Name="List Table 2 Accent 3"/>
  <w:LsdException Locked="false" Priority="48" Name="List Table 3 Accent 3"/>
  <w:LsdException Locked="false" Priority="49" Name="List Table 4 Accent 3"/>
  <w:LsdException Locked="false" Priority="50" Name="List Table 5 Dark Accent 3"/>
  <w:LsdException Locked="false" Priority="51"
   Name="List Table 6 Colorful Accent 3"/>
  <w:LsdException Locked="false" Priority="52"
   Name="List Table 7 Colorful Accent 3"/>
  <w:LsdException Locked="false" Priority="46"
   Name="List Table 1 Light Accent 4"/>
  <w:LsdException Locked="false" Priority="47" Name="List Table 2 Accent 4"/>
  <w:LsdException Locked="false" Priority="48" Name="List Table 3 Accent 4"/>
  <w:LsdException Locked="false" Priority="49" Name="List Table 4 Accent 4"/>
  <w:LsdException Locked="false" Priority="50" Name="List Table 5 Dark Accent 4"/>
  <w:LsdException Locked="false" Priority="51"
   Name="List Table 6 Colorful Accent 4"/>
  <w:LsdException Locked="false" Priority="52"
   Name="List Table 7 Colorful Accent 4"/>
  <w:LsdException Locked="false" Priority="46"
   Name="List Table 1 Light Accent 5"/>
  <w:LsdException Locked="false" Priority="47" Name="List Table 2 Accent 5"/>
  <w:LsdException Locked="false" Priority="48" Name="List Table 3 Accent 5"/>
  <w:LsdException Locked="false" Priority="49" Name="List Table 4 Accent 5"/>
  <w:LsdException Locked="false" Priority="50" Name="List Table 5 Dark Accent 5"/>
  <w:LsdException Locked="false" Priority="51"
   Name="List Table 6 Colorful Accent 5"/>
  <w:LsdException Locked="false" Priority="52"
   Name="List Table 7 Colorful Accent 5"/>
  <w:LsdException Locked="false" Priority="46"
   Name="List Table 1 Light Accent 6"/>
  <w:LsdException Locked="false" Priority="47" Name="List Table 2 Accent 6"/>
  <w:LsdException Locked="false" Priority="48" Name="List Table 3 Accent 6"/>
  <w:LsdException Locked="false" Priority="49" Name="List Table 4 Accent 6"/>
  <w:LsdException Locked="false" Priority="50" Name="List Table 5 Dark Accent 6"/>
  <w:LsdException Locked="false" Priority="51"
   Name="List Table 6 Colorful Accent 6"/>
  <w:LsdException Locked="false" Priority="52"
   Name="List Table 7 Colorful Accent 6"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Mention"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Smart Hyperlink"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Hashtag"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Unresolved Mention"/>
  <w:LsdException Locked="false" SemiHidden="true" UnhideWhenUsed="true"
   Name="Smart Link"/>
 </w:LatentStyles>
</xml><![endif]-->
<style>
<!--
 /* Font Definitions */
 @font-face
	{font-family:宋体;
	panose-1:2 1 6 0 3 1 1 1 1 1;
	mso-font-alt:SimSun;
	mso-font-charset:134;
	mso-generic-font-family:auto;
	mso-font-pitch:variable;
	mso-font-signature:3 680460288 22 0 262145 0;}
@font-face
	{font-family:黑体;
	panose-1:2 1 6 9 6 1 1 1 1 1;
	mso-font-alt:SimHei;
	mso-font-charset:134;
	mso-generic-font-family:modern;
	mso-font-pitch:fixed;
	mso-font-signature:-2147482945 953122042 22 0 262145 0;}
@font-face
	{font-family:"Cambria Math";
	panose-1:2 4 5 3 5 4 6 3 2 4;
	mso-font-charset:0;
	mso-generic-font-family:roman;
	mso-font-pitch:variable;
	mso-font-signature:3 0 0 0 1 0;}
@font-face
	{font-family:等线;
	panose-1:2 1 6 0 3 1 1 1 1 1;
	mso-font-alt:DengXian;
	mso-font-charset:134;
	mso-generic-font-family:auto;
	mso-font-pitch:variable;
	mso-font-signature:-1610612033 953122042 22 0 262159 0;}
@font-face
	{font-family:"\\@宋体";
	panose-1:2 1 6 0 3 1 1 1 1 1;
	mso-font-charset:134;
	mso-generic-font-family:auto;
	mso-font-pitch:variable;
	mso-font-signature:3 680460288 22 0 262145 0;}
@font-face
	{font-family:"\\@等线";
	panose-1:2 1 6 0 3 1 1 1 1 1;
	mso-font-alt:"\\@DengXian";
	mso-font-charset:134;
	mso-generic-font-family:auto;
	mso-font-pitch:variable;
	mso-font-signature:-1610612033 953122042 22 0 262159 0;}
@font-face
	{font-family:"\\@黑体";
	panose-1:2 1 6 0 3 1 1 1 1 1;
	mso-font-charset:134;
	mso-generic-font-family:modern;
	mso-font-pitch:fixed;
	mso-font-signature:-2147482945 953122042 22 0 262145 0;}
 /* Style Definitions */
 p.MsoNormal, li.MsoNormal, div.MsoNormal
	{mso-style-unhide:no;
	mso-style-qformat:yes;
	mso-style-parent:"";
	margin:0cm;
	margin-bottom:.0001pt;
	text-align:justify;
	text-justify:inter-ideograph;
	mso-pagination:none;
	font-size:10.5pt;
	mso-bidi-font-size:12.0pt;
	font-family:等线;
	mso-ascii-font-family:等线;
	mso-ascii-theme-font:minor-latin;
	mso-fareast-font-family:等线;
	mso-fareast-theme-font:minor-fareast;
	mso-hansi-font-family:等线;
	mso-hansi-theme-font:minor-latin;
	mso-bidi-font-family:"Times New Roman";
	mso-bidi-theme-font:minor-bidi;
	mso-font-kerning:1.0pt;}
h1
	{mso-style-priority:9;
	mso-style-unhide:no;
	mso-style-qformat:yes;
	mso-style-link:"标题 1 字符";
	mso-style-next:正文;
	margin-top:17.0pt;
	margin-right:0cm;
	margin-bottom:16.5pt;
	margin-left:0cm;
	text-align:justify;
	text-justify:inter-ideograph;
	line-height:240%;
	mso-pagination:lines-together;
	page-break-after:avoid;
	mso-outline-level:1;
	font-size:22.0pt;
	font-family:等线;
	mso-ascii-font-family:等线;
	mso-ascii-theme-font:minor-latin;
	mso-fareast-font-family:等线;
	mso-fareast-theme-font:minor-fareast;
	mso-hansi-font-family:等线;
	mso-hansi-theme-font:minor-latin;
	mso-bidi-font-family:宋体;
	mso-font-kerning:22.0pt;}
p.MsoHeader, li.MsoHeader, div.MsoHeader
	{mso-style-priority:99;
	mso-style-link:"页眉 字符";
	margin:0cm;
	margin-bottom:.0001pt;
	text-align:center;
	mso-pagination:none;
	tab-stops:center 207.65pt right 415.3pt;
	layout-grid-mode:char;
	border:none;
	mso-border-bottom-alt:solid windowtext .75pt;
	padding:0cm;
	mso-padding-alt:0cm 0cm 1.0pt 0cm;
	font-size:9.0pt;
	font-family:等线;
	mso-ascii-font-family:等线;
	mso-ascii-theme-font:minor-latin;
	mso-fareast-font-family:等线;
	mso-fareast-theme-font:minor-fareast;
	mso-hansi-font-family:等线;
	mso-hansi-theme-font:minor-latin;
	mso-bidi-font-family:"Times New Roman";
	mso-bidi-theme-font:minor-bidi;
	mso-font-kerning:1.0pt;}
p.MsoFooter, li.MsoFooter, div.MsoFooter
	{mso-style-priority:99;
	mso-style-link:"页脚 字符";
	margin:0cm;
	margin-bottom:.0001pt;
	mso-pagination:none;
	tab-stops:center 207.65pt right 415.3pt;
	layout-grid-mode:char;
	font-size:9.0pt;
	font-family:等线;
	mso-ascii-font-family:等线;
	mso-ascii-theme-font:minor-latin;
	mso-fareast-font-family:等线;
	mso-fareast-theme-font:minor-fareast;
	mso-hansi-font-family:等线;
	mso-hansi-theme-font:minor-latin;
	mso-bidi-font-family:"Times New Roman";
	mso-bidi-theme-font:minor-bidi;
	mso-font-kerning:1.0pt;}
span.1
	{mso-style-name:"标题 1 字符";
	mso-style-priority:9;
	mso-style-unhide:no;
	mso-style-locked:yes;
	mso-style-link:"标题 1";
	mso-ansi-font-size:22.0pt;
	mso-bidi-font-size:22.0pt;
	mso-font-kerning:22.0pt;
	font-weight:bold;}
p.msonormal0, li.msonormal0, div.msonormal0
	{mso-style-name:msonormal;
	mso-style-unhide:no;
	mso-margin-top-alt:auto;
	margin-right:0cm;
	mso-margin-bottom-alt:auto;
	margin-left:0cm;
	mso-pagination:widow-orphan;
	font-size:12.0pt;
	font-family:宋体;
	mso-bidi-font-family:宋体;}
span.a
	{mso-style-name:"页眉 字符";
	mso-style-priority:99;
	mso-style-unhide:no;
	mso-style-locked:yes;
	mso-style-link:页眉;
	mso-ansi-font-size:9.0pt;
	mso-bidi-font-size:9.0pt;
	font-family:等线;
	mso-ascii-font-family:等线;
	mso-ascii-theme-font:minor-latin;
	mso-fareast-font-family:等线;
	mso-fareast-theme-font:minor-fareast;
	mso-hansi-font-family:等线;
	mso-hansi-theme-font:minor-latin;
	mso-font-kerning:1.0pt;}
span.a0
	{mso-style-name:"页脚 字符";
	mso-style-priority:99;
	mso-style-unhide:no;
	mso-style-locked:yes;
	mso-style-link:页脚;
	mso-ansi-font-size:9.0pt;
	mso-bidi-font-size:9.0pt;
	font-family:等线;
	mso-ascii-font-family:等线;
	mso-ascii-theme-font:minor-latin;
	mso-fareast-font-family:等线;
	mso-fareast-theme-font:minor-fareast;
	mso-hansi-font-family:等线;
	mso-hansi-theme-font:minor-latin;
	mso-font-kerning:1.0pt;}
span.SpellE
	{mso-style-name:"";
	mso-spl-e:yes;}
span.GramE
	{mso-style-name:"";
	mso-gram-e:yes;}
.MsoChpDefault
	{mso-style-type:export-only;
	mso-default-props:yes;
	font-size:10.0pt;
	mso-ansi-font-size:10.0pt;
	mso-bidi-font-size:10.0pt;
	font-family:等线;
	mso-ascii-font-family:等线;
	mso-fareast-font-family:等线;
	mso-hansi-font-family:等线;
	mso-bidi-font-family:"Times New Roman";
	mso-bidi-theme-font:minor-bidi;
	mso-font-kerning:0pt;}
 /* Page Definitions */
 @page
	{mso-page-border-surround-header:no;
	mso-page-border-surround-footer:no;
	mso-footnote-separator:url("databaseDic.fld/header.htm") fs;
	mso-footnote-continuation-separator:url("databaseDic.fld/header.htm") fcs;
	mso-endnote-separator:url("databaseDic.fld/header.htm") es;
	mso-endnote-continuation-separator:url("databaseDic.fld/header.htm") ecs;}
@page WordSection1
	{size:595.0pt 842.0pt;
	margin:72.0pt 90.0pt 72.0pt 90.0pt;
	mso-header-margin:42.55pt;
	mso-footer-margin:49.6pt;
	mso-paper-source:0;
	layout-grid:15.6pt;}
div.WordSection1
	{page:WordSection1;}
-->
</style>
<!--[if gte mso 10]>
<style>
 /* Style Definitions */
 table.MsoNormalTable
	{mso-style-name:普通表格;
	mso-tstyle-rowband-size:0;
	mso-tstyle-colband-size:0;
	mso-style-noshow:yes;
	mso-style-priority:99;
	mso-style-parent:"";
	mso-padding-alt:0cm 5.4pt 0cm 5.4pt;
	mso-para-margin:0cm;
	mso-para-margin-bottom:.0001pt;
	mso-pagination:widow-orphan;
	font-size:10.0pt;
	font-family:等线;}
</style>
<![endif]--><!--[if gte mso 9]><xml>
 <o:shapedefaults v:ext="edit" spidmax="2049"/>
</xml><![endif]--><!--[if gte mso 9]><xml>
 <o:shapelayout v:ext="edit">
  <o:idmap v:ext="edit" data="1"/>
 </o:shapelayout></xml><![endif]-->
</head>

<body lang=ZH-CN style=''tab-interval:21.0pt;text-justify-trim:punctuation''>

<div class=WordSection1 style=''layout-grid:15.6pt''>

<p class=MsoNormal><span lang=EN-US style=''font-size:26.0pt''><o:p>&nbsp;</o:p></span></p>

<p class=MsoNormal><span lang=EN-US style=''font-size:26.0pt''><o:p>&nbsp;</o:p></span></p>

<h1 align=center style=''text-align:center''><span lang=EN-US style=''font-size:
24.0pt;line-height:240%''>XXXX</span><span style=''font-size:24.0pt;line-height:
240%''>项目<span lang=EN-US><o:p></o:p></span></span></h1>

<p class=MsoNormal align=center style=''text-align:center;text-indent:21.0pt;
line-height:150%;mso-pagination:widow-orphan''><span style=''font-size:36.0pt;
line-height:150%;font-family:黑体;mso-bidi-font-family:宋体;mso-font-kerning:0pt''>数据库设计文档<span
lang=EN-US><o:p></o:p></span></span></p>

<p class=MsoNormal><span lang=EN-US><o:p>&nbsp;</o:p></span></p>

<p class=MsoNormal><span lang=EN-US><o:p>&nbsp;</o:p></span></p>

<p class=MsoNormal><span lang=EN-US><o:p>&nbsp;</o:p></span></p>

<p class=MsoNormal><span lang=EN-US><o:p>&nbsp;</o:p></span></p>

<p class=MsoNormal><span lang=EN-US><o:p>&nbsp;</o:p></span></p>

<p class=MsoNormal><span lang=EN-US><o:p>&nbsp;</o:p></span></p>

<p class=MsoNormal><span lang=EN-US><o:p>&nbsp;</o:p></span></p>

<p class=MsoNormal><span lang=EN-US><o:p>&nbsp;</o:p></span></p>

<p class=MsoNormal><span lang=EN-US><o:p>&nbsp;</o:p></span></p>

<p class=MsoNormal><span lang=EN-US><o:p>&nbsp;</o:p></span></p>

<p class=MsoNormal><span lang=EN-US><o:p>&nbsp;</o:p></span></p>

<p class=MsoNormal><span lang=EN-US><o:p>&nbsp;</o:p></span></p>

<p class=MsoNormal><span lang=EN-US><o:p>&nbsp;</o:p></span></p>

<p class=MsoNormal><span lang=EN-US><o:p>&nbsp;</o:p></span></p>

<p class=MsoNormal><span lang=EN-US><o:p>&nbsp;</o:p></span></p>

<p class=MsoNormal><span lang=EN-US><o:p>&nbsp;</o:p></span></p>

<p class=MsoNormal><span lang=EN-US><o:p>&nbsp;</o:p></span></p>

<p class=MsoNormal><span lang=EN-US><o:p>&nbsp;</o:p></span></p>

<p class=MsoNormal><span lang=EN-US><o:p>&nbsp;</o:p></span></p>

<p class=MsoNormal align=center style=''text-align:center''><span
style=''font-size:16.0pt''>威海联亚软件开发服务有限公司<span lang=EN-US><o:p></o:p></span></span></p>

<p class=MsoNormal align=center style=''text-align:center''><span lang=EN-US
style=''font-size:16.0pt''>${currentDate}<o:p></o:p></span></p>

<span lang=EN-US style=''font-size:16.0pt;font-family:等线;mso-ascii-theme-font:
minor-latin;mso-fareast-theme-font:minor-fareast;mso-hansi-theme-font:minor-latin;
mso-bidi-font-family:"Times New Roman";mso-bidi-theme-font:minor-bidi;
mso-font-kerning:1.0pt;mso-ansi-language:EN-US;mso-fareast-language:ZH-CN;
mso-bidi-language:AR-SA''><br clear=all style=''mso-special-character:line-break;
page-break-before:always''>
</span>

<p class=MsoNormal align=left style=''text-align:left;mso-pagination:widow-orphan''><span
lang=EN-US style=''font-size:16.0pt''><o:p>&nbsp;</o:p></span></p>

 <#list tableList as table>

<p class=MsoNormal align=center style=''text-align:center''><span lang=EN-US
style=''font-size:14.0pt''><span class=SpellE><span class=GramE>${table.remarks ! ''''}</span></span>
</span><span style=''font-size:14.0pt''>（<span lang=EN-US>${table.tableName}</span>）<span
lang=EN-US><o:p></o:p></span></span></p>

<table class=MsoNormalTable border=1 cellspacing=0 cellpadding=0 width=568
 style=''border-collapse:collapse;mso-table-layout-alt:fixed;border:none;
 mso-border-alt:solid windowtext 1.5pt;mso-yfti-tbllook:1184;mso-padding-alt:
 0cm 5.4pt 0cm 5.4pt;mso-border-insideh:.75pt solid windowtext;mso-border-insidev:
 .75pt solid windowtext''>
 <tr style=''mso-yfti-irow:0;mso-yfti-firstrow:yes;height:13.5pt''>
  <td width=44 valign=top style=''width:32.7pt;border-top:1.5pt;border-left:
  1.5pt;border-bottom:1.0pt;border-right:1.0pt;border-color:windowtext;
  border-style:solid;mso-border-top-alt:1.5pt;mso-border-left-alt:1.5pt;
  mso-border-bottom-alt:.75pt;mso-border-right-alt:.75pt;mso-border-color-alt:
  windowtext;mso-border-style-alt:solid;padding:0cm 5.4pt 0cm 5.4pt;height:
  13.5pt''>
  <p class=MsoNormal><b style=''mso-bidi-font-weight:normal''><span
  style=''font-size:9.0pt;font-family:宋体''>序号<span lang=EN-US><o:p></o:p></span></span></b></p>
  </td>
  <td width=115 valign=top style=''width:86.05pt;border-top:solid windowtext 1.5pt;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-left-alt:solid windowtext .75pt;mso-border-alt:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;
  height:13.5pt''>
  <p class=MsoNormal><b style=''mso-bidi-font-weight:normal''><span
  style=''font-size:9.0pt;font-family:宋体''>字段名<span lang=EN-US><o:p></o:p></span></span></b></p>
  </td>
  <td width=79 valign=top style=''width:59.4pt;border-top:solid windowtext 1.5pt;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-left-alt:solid windowtext .75pt;mso-border-alt:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;
  height:13.5pt''>
  <p class=MsoNormal><b style=''mso-bidi-font-weight:normal''><span
  style=''font-size:9.0pt;font-family:宋体''>标识符<span lang=EN-US><o:p></o:p></span></span></b></p>
  </td>
  <td width=118 valign=top style=''width:88.85pt;border-top:solid windowtext 1.5pt;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-left-alt:solid windowtext .75pt;mso-border-alt:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;
  height:13.5pt''>
  <p class=MsoNormal><b style=''mso-bidi-font-weight:normal''><span
  style=''font-size:9.0pt;font-family:宋体''>类型<span lang=EN-US><o:p></o:p></span></span></b></p>
  </td>
  <td width=57 valign=top style=''width:42.9pt;border-top:solid windowtext 1.5pt;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-left-alt:solid windowtext .75pt;mso-border-alt:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;
  height:13.5pt''>
  <p class=MsoNormal><b style=''mso-bidi-font-weight:normal''><span
  style=''font-size:9.0pt;font-family:宋体''>约束<span lang=EN-US><o:p></o:p></span></span></b></p>
  </td>
  <td width=155 valign=top style=''width:116.1pt;border-top:solid windowtext 1.5pt;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
  mso-border-left-alt:solid windowtext .75pt;mso-border-top-alt:1.5pt;
  mso-border-left-alt:.75pt;mso-border-bottom-alt:.75pt;mso-border-right-alt:
  1.5pt;mso-border-color-alt:windowtext;mso-border-style-alt:solid;padding:
  0cm 5.4pt 0cm 5.4pt;height:13.5pt''>
  <p class=MsoNormal><b style=''mso-bidi-font-weight:normal''><span
  style=''font-size:9.0pt;font-family:宋体''>备注<span lang=EN-US><o:p></o:p></span></span></b></p>
  </td>
 </tr>

 <#if table.primaryKeyList??>

 <#list table.primaryKeyList as primaryKey>

 <tr style=''mso-yfti-irow:1;mso-yfti-lastrow:yes;height:13.5pt''>
  <td width=44 style=''width:32.7pt;border-top:none;border-left:solid windowtext 1.5pt;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-alt:solid windowtext .75pt;
  mso-border-left-alt:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;
  height:13.5pt''>
  <p class=MsoNormal align=center style=''text-align:center''><span lang=EN-US
  style=''font-size:9.0pt;font-family:宋体''>${primaryKey_index + 1}<o:p></o:p></span></p>
  </td>
  <td width=115 style=''width:86.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  mso-border-alt:solid windowtext .75pt;padding:0cm 5.4pt 0cm 5.4pt;height:
  13.5pt''>
  <p class=MsoNormal align=left style=''text-align:left''><span style=''font-size:
  9.0pt;font-family:宋体''>${primaryKey.remarks ! ''''}<span lang=EN-US><o:p></o:p></span></span></p>
  </td>
  <td width=79 style=''width:59.4pt;border-top:none;border-left:none;border-bottom:
  solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;mso-border-top-alt:
  solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;mso-border-alt:
  solid windowtext .75pt;padding:0cm 5.4pt 0cm 5.4pt;height:13.5pt''>
  <p class=MsoNormal align=left style=''text-align:left''><span lang=EN-US
  style=''font-size:9.0pt;font-family:宋体''>${primaryKey.columnName}<o:p></o:p></span></p>
  </td>
  <td width=118 style=''width:88.85pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  mso-border-alt:solid windowtext .75pt;padding:0cm 5.4pt 0cm 5.4pt;height:
  13.5pt''>
  <p class=MsoNormal align=left style=''text-align:left''><span class=GramE><span
  lang=EN-US style=''font-size:9.0pt;font-family:宋体''>${primaryKey.typeName}(${(primaryKey.typeName == ''NUMBER'' && primaryKey.columnSize == 0 && primaryKey.decimalDigits == -127) ? string( ''*'' , primaryKey.columnSize)})</span></span></p>
  </td>
  <td width=57 style=''width:42.9pt;border-top:none;border-left:none;border-bottom:
  solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;mso-border-top-alt:
  solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;mso-border-alt:
  solid windowtext .75pt;padding:0cm 5.4pt 0cm 5.4pt;height:13.5pt''>
  <p class=MsoNormal align=left style=''text-align:left''><span style=''font-size:
  9.0pt;font-family:宋体''>主键<span lang=EN-US><o:p></o:p></span></span></p>
  </td>
  <td width=155 style=''width:116.1pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  mso-border-alt:solid windowtext .75pt;mso-border-right-alt:solid windowtext 1.5pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:13.5pt''>
  <p class=MsoNormal align=left style=''text-align:left''><span lang=EN-US
  style=''font-size:9.0pt;font-family:宋体''><o:p>&nbsp;</o:p></span></p>
  </td>
 </tr>
 </#list>
 </#if>


 <#if table.baseColumnList??>

  <#list table.baseColumnList as column>

   <tr style=''mso-yfti-irow:1;mso-yfti-lastrow:yes;height:13.5pt''>
    <td width=44 style=''width:32.7pt;border-top:none;border-left:solid windowtext 1.5pt;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-alt:solid windowtext .75pt;
  mso-border-left-alt:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;
  height:13.5pt''>
     <p class=MsoNormal align=center style=''text-align:center''><span lang=EN-US
                                                                     style=''font-size:9.0pt;font-family:宋体''>${column_index  + 1}<o:p></o:p></span></p>
    </td>
    <td width=115 style=''width:86.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  mso-border-alt:solid windowtext .75pt;padding:0cm 5.4pt 0cm 5.4pt;height:
  13.5pt''>
     <p class=MsoNormal align=left style=''text-align:left''><span style=''font-size:
  9.0pt;font-family:宋体''>${column.remarks ! ''''}<span lang=EN-US><o:p></o:p></span></span></p>
    </td>
    <td width=79 style=''width:59.4pt;border-top:none;border-left:none;border-bottom:
  solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;mso-border-top-alt:
  solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;mso-border-alt:
  solid windowtext .75pt;padding:0cm 5.4pt 0cm 5.4pt;height:13.5pt''>
     <p class=MsoNormal align=left style=''text-align:left''><span lang=EN-US
                                                                 style=''font-size:9.0pt;font-family:宋体''>${column.columnName}<o:p></o:p></span></p>
    </td>
    <td width=118 style=''width:88.85pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  mso-border-alt:solid windowtext .75pt;padding:0cm 5.4pt 0cm 5.4pt;height:
  13.5pt''>
     <p class=MsoNormal align=left style=''text-align:left''><span class=GramE><span
               lang=EN-US style=''font-size:9.0pt;font-family:宋体''>${column.typeName}(${(column.typeName == ''NUMBER'' && column.columnSize == 0 && column.decimalDigits == -127) ? string( ''*'' , column.columnSize)})</span></span></p>
    </td>
    <td width=57 style=''width:42.9pt;border-top:none;border-left:none;border-bottom:
  solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;mso-border-top-alt:
  solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;mso-border-alt:
  solid windowtext .75pt;padding:0cm 5.4pt 0cm 5.4pt;height:13.5pt''>
     <p class=MsoNormal align=left style=''text-align:left''><span style=''font-size:
  9.0pt;font-family:宋体''>${column.unique?? ?string(''唯一索引'', '''')}${column.importKey?string('' 外键'', '''')}${(column.indexed && column.unique != true)?string('' 索引'', '''')}<span lang=EN-US><o:p></o:p></span></span></p>
    </td>
    <td width=155 style=''width:116.1pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  mso-border-alt:solid windowtext .75pt;mso-border-right-alt:solid windowtext 1.5pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:13.5pt''>
     <p class=MsoNormal align=left style=''text-align:left''><span lang=EN-US
                                                                 style=''font-size:9.0pt;font-family:宋体''><o:p>&nbsp;</o:p></span></p>
    </td>
   </tr>
  </#list>
 </#if>

</table>

 </#list>


<p class=MsoNormal align=center style=''text-align:center''><span lang=EN-US
style=''font-size:16.0pt''><o:p>&nbsp;</o:p></span></p>

</div>

</body>

</html>
', 1, sysdate(), null, null, 0, null, 'template_db_dict', 1);
INSERT INTO db_code_template (template_id, name, language, filename_suffix, remark, template, create_user_id, create_time, update_user_id, update_time, delete_yn, seq, template_type, group_id) VALUES (1429132411928610, '数据库字典(Excel)', 'xml', '', null, '<?xml version="1.0"?>
<?mso-application progid="Excel.Sheet"?>
<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet"
 xmlns:o="urn:schemas-microsoft-com:office:office"
 xmlns:x="urn:schemas-microsoft-com:office:excel"
 xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
 xmlns:html="http://www.w3.org/TR/REC-html40">
 <DocumentProperties xmlns="urn:schemas-microsoft-com:office:office">
  <Author>GCCodeGenerator</Author>
  <LastAuthor>GCCodeGenerator</LastAuthor>
  <Created>2021-05-19T08:54:15Z</Created>
  <LastSaved>2021-05-19T08:54:15Z</LastSaved>
  <Version>16.00</Version>
 </DocumentProperties>
 <OfficeDocumentSettings xmlns="urn:schemas-microsoft-com:office:office">
  <AllowPNG/>
 </OfficeDocumentSettings>
 <ExcelWorkbook xmlns="urn:schemas-microsoft-com:office:excel">
  <WindowHeight>8928</WindowHeight>
  <WindowWidth>23040</WindowWidth>
  <WindowTopX>32767</WindowTopX>
  <WindowTopY>32767</WindowTopY>
  <ProtectStructure>False</ProtectStructure>
  <ProtectWindows>False</ProtectWindows>
 </ExcelWorkbook>
 <Styles>
  <Style ss:ID="Default" ss:Name="Normal">
   <Alignment ss:Vertical="Center"/>
   <Borders/>
   <Font ss:FontName="等线" x:CharSet="134" ss:Size="11" ss:Color="#000000"/>
   <Interior/>
   <NumberFormat/>
   <Protection/>
  </Style>
  <Style ss:ID="m2553513267088">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="2"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Font ss:FontName="Calibri" x:Family="Swiss" ss:Size="11" ss:Color="#008000"/>
   <Interior ss:Color="#C6EFCE" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="s69">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
   <Borders>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Font ss:FontName="Calibri" x:Family="Swiss" ss:Size="11" ss:Color="#008000"/>
   <Interior ss:Color="#C6EFCE" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="s70">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Font ss:FontName="Calibri" x:Family="Swiss" ss:Size="11" ss:Color="#008000"/>
   <Interior ss:Color="#C6EFCE" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="s71">
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
  </Style>
  <Style ss:ID="s72">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Font ss:FontName="Calibri" x:Family="Swiss" ss:Size="11" ss:Color="#000000"/>
  </Style>
  <Style ss:ID="s73">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Font ss:FontName="Calibri" x:Family="Swiss" ss:Size="11" ss:Color="#000000"/>
  </Style>
 </Styles>
 <Worksheet ss:Name="数据库设计文档">
  <Table ss:ExpandedColumnCount="7" ss:ExpandedRowCount="${tableList?size * 2 + columnSize + 20}" x:FullColumns="1"
   x:FullRows="1" ss:DefaultRowHeight="13.8">
   <Column ss:Index="2" ss:AutoFitWidth="0" ss:Width="142.80000000000001"/>
   <Column ss:AutoFitWidth="0" ss:Width="141"/>
   <Column ss:AutoFitWidth="0" ss:Width="61.8"/>
   <Column ss:AutoFitWidth="0" ss:Width="119.4"/>
   <Column ss:Index="7" ss:AutoFitWidth="0" ss:Width="90"/>
   <#list tableList as table>
  <Row ss:AutoFitHeight="0" ss:Height="25.049999999999997">
    <Cell ss:MergeAcross="6" ss:StyleID="m2553513267088"><Data ss:Type="String">${table.tableName} ${table.remarks ! ''''}</Data></Cell>
   </Row>
  <Row ss:AutoFitHeight="0" ss:Height="14.55">
    <Cell ss:StyleID="s69"><Data ss:Type="String">序号</Data></Cell>
    <Cell ss:StyleID="s70"><Data ss:Type="String">字段</Data></Cell>
    <Cell ss:StyleID="s70"><Data ss:Type="String">描述</Data></Cell>
    <Cell ss:StyleID="s70"><Data ss:Type="String">主键</Data></Cell>
    <Cell ss:StyleID="s70"><Data ss:Type="String">类型</Data></Cell>
    <Cell ss:StyleID="s70"><Data ss:Type="String">是否为空</Data></Cell>
    <Cell ss:StyleID="s70"><Data ss:Type="String">默认值</Data></Cell>
   </Row>
   <#if table.primaryKeyList??>
    <#list table.primaryKeyList as primaryKey>
   <Row ss:AutoFitHeight="0" ss:Height="16.95">
    <Cell ss:StyleID="s71"><Data ss:Type="Number">${primaryKey_index + 1}</Data></Cell>
    <Cell ss:StyleID="s72"><Data ss:Type="String">${primaryKey.columnName}</Data></Cell>
    <Cell ss:StyleID="s73"><Data ss:Type="String">${primaryKey.remarks ! ''''}</Data></Cell>
    <Cell ss:StyleID="s73"><Data ss:Type="String">Y</Data></Cell>
    <Cell ss:StyleID="s73"><Data ss:Type="String">${primaryKey.typeName}(${(primaryKey.typeName == ''NUMBER'' && primaryKey.columnSize == 0 && primaryKey.decimalDigits == -127) ? string( ''*'' , primaryKey.columnSize)})</Data></Cell>
    <Cell ss:StyleID="s73"><Data ss:Type="String">N</Data></Cell>
    <Cell ss:StyleID="s73"><Data ss:Type="String">${primaryKey.columnDef ! ''''}</Data></Cell>
   </Row>
    </#list>
   </#if>

   <#if table.baseColumnList??>
    <#list table.baseColumnList as column>
      <Row ss:AutoFitHeight="0" ss:Height="16.95">
    <Cell ss:StyleID="s71"><Data ss:Type="Number"><#if table.primaryKeyList??>${table.primaryKeyList?size + column_index + 1}<#else >${column_index + 1}</#if></Data></Cell>
    <Cell ss:StyleID="s72"><Data ss:Type="String">${column.columnName}</Data></Cell>
    <Cell ss:StyleID="s73"><Data ss:Type="String">${column.remarks ! ''''}</Data></Cell>
    <Cell ss:StyleID="s73"><Data ss:Type="String"></Data></Cell>
    <Cell ss:StyleID="s73"><Data ss:Type="String">${column.typeName}(${(column.typeName == ''NUMBER'' && column.columnSize == 0 && column.decimalDigits == -127) ? string( ''*'' , column.columnSize)})</Data></Cell>
    <Cell ss:StyleID="s73"><Data ss:Type="String">${(column.nullable == 0) ? string(''N'', '''')}</Data></Cell>
    <Cell ss:StyleID="s73"><Data ss:Type="String">${column.columnDef ! ''''}</Data></Cell>
   </Row>
    </#list>
   </#if>
   </#list>

  </Table>
  <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
   <PageSetup>
    <Header x:Margin="0.3"/>
    <Footer x:Margin="0.3"/>
    <PageMargins x:Bottom="0.75" x:Left="0.7" x:Right="0.7" x:Top="0.75"/>
   </PageSetup>
   <Unsynced/>
   <Print>
    <ValidPrinterInfo/>
    <PaperSizeIndex>9</PaperSizeIndex>
    <HorizontalResolution>600</HorizontalResolution>
    <VerticalResolution>600</VerticalResolution>
   </Print>
   <Selected/>
   <Panes>
    <Pane>
     <Number>3</Number>
     <ActiveRow>2</ActiveRow>
    </Pane>
   </Panes>
   <ProtectObjects>False</ProtectObjects>
   <ProtectScenarios>False</ProtectScenarios>
  </WorksheetOptions>
 </Worksheet>
</Workbook>
', 1, sysdate(), null, null, 0, null, 'template_db_dict', 1);



-- 模板分组表
drop table if exists `db_code_template_group`;
create table db_code_template_group
(
    group_id       bigint       not null
        primary key,
    group_name     varchar(255) not null,
    seq            int          not null,
    create_time    datetime     not null,
    create_user_id bigint       not null,
    update_time    datetime     null,
    update_user_id bigint       null
)
    comment '模板分组表';

insert into db_code_template_group (group_id, group_name, seq, create_time, create_user_id, update_time, update_user_id) values (1, '系统内置模板', 1, sysdate(), 1, null, null);

-- 模板用户组关联关系表
drop table if exists `db_code_template_user_group`;
create table db_code_template_user_group
(
    template_id    bigint   not null,
    group_id       bigint   not null,
    create_time    datetime not null,
    create_user_id bigint   not null,
    primary key (template_id, group_id)
)
    comment '模板用户组关联关系表';
