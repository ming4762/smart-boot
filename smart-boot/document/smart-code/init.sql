
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
