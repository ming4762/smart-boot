-- 创建人员信息表
drop table if exists `sys_user`;
create table sys_user
(
    user_id        bigint               not null comment '用户ID'
        primary key,
    username       varchar(50)          not null comment '用户名',
    full_name      varchar(50)          not null comment '姓名',
    password       varchar(255)         not null comment '密码',
    email          varchar(255)         null comment '邮箱',
    mobile         varchar(50)          null comment '手机',
    user_type      char(2)              not null comment '用户类型（10：系统用户，20：业务用户）',
    create_user_id bigint               not null comment '创建人员ID',
    create_time    datetime             not null comment '创建时间',
    update_user_id int                  null comment '更新人员ID',
    update_time    datetime             null comment '更新时间',
    seq            int                  not null comment '序号',
    delete_yn      tinyint(1) default 0 not null comment '删除标识',
    use_yn         tinyint(1) default 1 not null comment '启用标识',
    constraint uni_user_name
        unique (username) comment '用户名唯一键'
)
    comment '系统用户表';

INSERT INTO sys_user (user_id, username, full_name, password, email, mobile, user_type, create_user_id, create_time, update_user_id, update_time, seq, delete_yn, use_yn) VALUES (1, 'admin', '超级管理员', 'b877bef66e0ffbf77d18118f3281644f73b6d2994c197a6ac9afa4b3e385748e', null, null, '10', 1, sysdate(), null, null, 1, 0, 1);

-- 人员账户信息
drop table if exists `sys_user_account`;
create table sys_user_account
(
    user_id                      bigint                  not null
        primary key,
    login_fail_time              int        default 0    not null,
    account_status               char(2)    default '10' not null comment '10：正常
20：多次登录失败锁定
30：超出指定时间未登录锁定
40：超出指定时间未修改密码锁定',
    last_login_time              datetime                not null comment '上次登录时间',
    initial_password_yn          tinyint(1) default 1    not null comment '是否是初始化密码',
    create_time                  datetime                null,
    ip_white_list                text                    null comment 'IP白名单，多个IP以逗号分隔',
    max_connections              int        default 0    not null comment '最大访问连接数，单个用户可同时登录的数量，0：不限制',
    max_days_since_login         int        default 0    not null comment '指定天数未登录账户锁定，0：永不锁定',
    password_life_days           int        default 0    not null comment '密码必须修改的天使，0：不限制',
    max_connections_policy       char(2)    default '10' not null comment '超出最大连接数执行策略：
10：不允许登录
20：最早登录用户登出',
    login_fail_time_limit        int        default 0    not null comment '登录失败锁定次数，0永不锁定',
    password_modify_time         datetime                not null comment '密码修改时间',
    password_error_unlock_second bigint     default 0    not null comment '多次输入密码错误锁定后，指定秒后自动解锁，0：永不自动解锁',
    lock_time                    datetime                null comment '账户锁定时间'
);

INSERT INTO sys_user_account (user_id, login_fail_time, account_status, last_login_time, initial_password_yn, create_time, ip_white_list, max_connections, max_days_since_login, password_life_days, max_connections_policy, login_fail_time_limit, password_modify_time, password_error_unlock_second, lock_time) VALUES (1, 0, '10', sysdate(), 0, sysdate(), null, 0, 0, 0, '20', 3, sysdate(), 120, null);

-- 创建用户组表
drop table if exists `sys_user_group`;
create table sys_user_group
(
    group_id       bigint               not null
        primary key,
    group_name     varchar(50)          not null comment '用户组名称',
    group_code     varchar(50)          not null comment '用户组编码',
    remark         varchar(255)         null comment '备注',
    seq            int                  null comment '序号',
    use_yn         tinyint(1) default 1 not null comment '是否启用',
    create_user_id bigint               null,
    create_time    datetime             null,
    update_user_id bigint               null,
    update_time    datetime             null,
    constraint uk_group_code
        unique (group_code)
);

-- 创建用户组用户关联关系表
drop table if exists `sys_user_group_user`;
create table sys_user_group_user
(
    user_id        bigint     not null comment '用户ID',
    user_group_id  bigint     not null comment '用户组ID',
    use_yn         tinyint(1) not null comment '是否启用',
    create_time    datetime   null,
    create_user_id bigint     null,
    primary key (user_id, user_group_id)
);


-- 系统功能表
drop table if exists `sys_function`;
create table sys_function
(
    function_id          bigint               not null comment '功能ID'
        primary key,
    parent_id            bigint               not null comment '上级ID',
    function_name        varchar(50)          not null comment '功能名称',
    function_type        char(2)              not null comment '功能类型（10：目录 20：菜单 30：功能）',
    i18n_code            varchar(255)         null comment '国际化编码',
    seq                  int                  null comment '序号',
    create_time          datetime             not null comment '创建时间',
    create_user_id       bigint               null comment '创建人员ID',
    update_time          datetime             null comment '更新时间',
    update_user_id       bigint               null comment '更新人员ID',
    url                  varchar(500)         null comment 'url',
    permission           varchar(255)         null comment '权限',
    is_menu              tinyint(1) default 1 not null comment '是否菜单',
    internal_or_external tinyint(1) default 0 not null comment '外链菜单打开方式 0/内部打开 1/外部打开',
    data_rule            tinyint(1) default 0 not null comment '是否配置数据权限',
    http_method          varchar(10)          null,
    remark               varchar(255)         null comment '描述',
    icon                 varchar(50)          null comment '图标'
)
    comment '系统功能表';

create index idx_parent_id
    on sys_function (parent_id);

INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (2, 0, '系统管理', '10', '{menu.system.system}', 100, sysdate(), 1, null, null, null, null, 1, 0, 0, null, null, 'SettingOutlined');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (21, 2, '用户管理', '20', '{menu.system.user.manager}', 110, sysdate(), 1, null, null, '/sys/userList', null, 1, 0, 0, null, null, 'UserOutlined');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (22, 2, '角色管理', '20', '{menu.system.role.manager}', 120, sysdate(), 1, null, null, '/sys/roleList', null, 1, 0, 0, null, null, 'UserSwitchOutlined');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (23, 2, '功能管理', '20', '{menu.system.function.manager}', 130, sysdate(), 1, null, null, '/sys/functionList', null, 1, 0, 0, null, null, 'AppstoreOutlined');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (24, 2, '用户组管理', '20', '{menu.system.userGroup.manager}', 115, sysdate(), 1, null, null, '/sys/userGroupList', null, 1, 0, 0, null, null, 'UsergroupAddOutlined');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (25, 2, '国际化管理', '20', '{menu.system.i18n.manager}', 140, sysdate(), 1, null, null, '/sys/i18n', null, 1, 0, 0, null, null, 'TranslationOutlined');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (211, 21, '添加用户', '30', null, 10, sysdate(), 1, null, 1, null, 'sys:user:save', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (212, 21, '删除用户', '30', null, 20, sysdate(), 1, null, null, null, 'sys:user:delete', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (213, 21, '修改用户', '30', null, 30, sysdate(), 1, null, null, null, 'sys:user:update', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (214, 21, '设置用户角色', '30', null, 40, sysdate(), 1, null, null, null, 'sys:user:setRole', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (215, 21, '设置用户启用状态', '30', null, 50, sysdate(), 1, null, null, null, 'sys:user:setUseYn', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (216, 21, '创建账户', '30', null, 60, sysdate(), 1, null, null, '/sys/user/createAccount', 'sys:account:add', 1, 0, 0, 'POST', null, null);
insert into sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, http_method, remark, icon) values (217, 21, '查看账户信息', '30', null, 70, sysdate(), 1, null, null, null, 'sys:account:query', 'POST', null, null);
insert into sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, http_method, remark, icon) values (218, 21, '更新账户信息', '30', null, 80, sysdate(), 1, null, null, '/sys/user/updateAccount', 'sys:account:update', 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (221, 24, '设置用户', '30', null, 40, sysdate(), 1, null, null, null, 'sys:userGroup:setUser', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (222, 22, '添加角色', '30', null, 10, sysdate(), 1, '2021-12-17 11:12:41', 1, null, 'sys:role:save', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (223, 22, '删除角色', '30', null, 20, sysdate(), 1, null, null, null, 'sys:role:delete', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (224, 22, '修改角色', '30', null, 30, sysdate(), 1, null, null, null, 'sys:role:update', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (231, 22, '设置功能', '30', null, 40, sysdate(), 1, null, null, null, 'sys:role:setFunction', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (232, 22, '设置用户', '30', null, 50, sysdate(), 1, null, null, null, 'sys:role:setRoleUser', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (233, 23, '添加功能', '30', null, 10, sysdate(), 1, null, null, null, 'sys:function', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (234, 23, '删除功能', '30', null, 20, sysdate(), 1, null, null, null, 'sys:function:delete', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (235, 23, '修改功能', '30', null, 30, sysdate(), 1, null, null, null, 'sys:function:update', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (241, 24, '添加用户组', '30', null, 10, sysdate(), 1, null, null, null, 'sys:userGroup:save', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (242, 24, '删除用户组', '30', null, 20, sysdate(), 1, null, null, null, 'sys:userGroup:delete', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (243, 24, '修改用户组', '30', null, 30, sysdate(), 1, null, null, null, 'sys:userGroup:update', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (251, 25, '新增', '30', null, 10, sysdate(), 1, null, null, null, 'sys:i18n:save', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (252, 25, '删除', '30', null, 20, sysdate(), 1, null, null, null, 'sys:i18n:delete', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (253, 25, '修改', '30', null, 30, sysdate(), 1, null, null, null, 'sys:i18n:update', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1459185009033250, 0, '系统监控', '10', '{menu.monitor.main}', 300, sysdate(), 1, null, null, null, null, 1, 0, 0, null, null, 'MonitorOutlined');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1459185233428514, 1459185009033250, '在线用户', '20', '{menu.monitor.onlineUser}', 10, sysdate(), 1, null, null, '/monitor/onlineUserList', null, 1, 0, 0, null, null, 'TeamOutlined');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1459360605667362, 1459185233428514, '用户下线', '30', null, 10, sysdate(), 1, null, null, '/auth/offline', 'sys:auth:offline', 1, 0, 0, 'POST', null, '');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1459380941750306, 1459185009033250, '系统日志', '20', '{menu.monitor.sysLog}', 20, sysdate(), 1, null, null, '/sys/logList', null, 1, 0, 0, null, null, 'HddOutlined');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1485797939216418, 1459185009033250, '异常信息', '20', '{menu.monitor.exception}', 30, sysdate(), 1, null, 1, '/sys/exception', null, 1, 0, 0, null, null, 'AlertOutlined');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1485801099624482, 1485797939216418, '查询异常信息', '30', null, 10, sysdate(), 1, null, 1, '/sys/exception/list;/sys/exception/getById', 'sys:exception:query', 1, 0, 0, null, null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1461165917995042, 2, '数据字典', '20', '{menu.system.dataDict.manager}', 150, sysdate(), 1, null, null, '/sys/dataDict', null, 1, 0, 0, null, null, 'HddOutlined');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1462978192867362, 1461165917995042, '添加字典项', '30', null, 40, sysdate(), 1, null, null, null, 'sys:dictItem:save', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1462978239004706, 1461165917995042, '修改字典项', '30', null, 50, sysdate(), 1, null, null, null, 'sys:dictItem:update', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1462978304016418, 1461165917995042, '删除字典项', '30', null, 60, sysdate(), 1, null, null, null, 'sys:dictItem:delete', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1462978599714850, 1461165917995042, '添加字典', '30', null, 10, sysdate(), 1, null, null, null, 'sys:dict:save', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1462978645852194, 1461165917995042, '修改字典', '30', null, 20, sysdate(), 1, null, null, null, 'sys:dict:edit', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1462978698280994, 1461165917995042, '删除字典', '30', null, 30, sysdate(), 1, null, null, null, 'sys:dict:delete', 1, 0, 0, 'POST', null, null);

-- 创建角色表
drop table if exists `sys_role`;
create table sys_role
(
    role_id        bigint               not null
        primary key,
    role_name      varchar(50)          not null comment '角色名称',
    role_code      varchar(50)          not null comment '角色编码',
    remark         varchar(255)         null comment '备注',
    use_yn         tinyint(1) default 1 not null comment '是否启用',
    role_type      char(2)              null comment '角色类型',
    create_user_id bigint               null comment '创建人员ID',
    create_time    datetime             null comment '创建时间',
    update_user_id bigint               null comment '更新人员ID',
    update_time    datetime             null comment '更新时间',
    seq            int                  null comment '序号'
);
INSERT INTO sys_role (role_id, role_name, role_code, remark, use_yn, role_type, create_user_id, create_time, update_user_id, update_time, seq) VALUES (1, '超级管理员', 'SUPERADMIN', '最高权限', 1, '10', null, sysdate(), null, null, 1);
INSERT INTO sys_role (role_id, role_name, role_code, remark, use_yn, role_type, create_user_id, create_time, update_user_id, update_time, seq) VALUES (2, '普通用户', 'COMMON_USER', null, 1, '10', null, sysdate(), null, null, 20);

-- 创建人员角色关系表
drop table if exists `sys_user_role`;
create table sys_user_role
(
    user_id        bigint               not null comment '用户ID',
    role_id        bigint               not null comment '角色ID',
    enable         tinyint(1) default 1 not null comment '是否启用',
    create_time    datetime             not null comment '创建时间',
    create_user_id bigint               null comment '创建人员Id',
    primary key (user_id, role_id)
);
INSERT INTO sys_user_role (user_id, role_id, enable, create_time, create_user_id) VALUES (1, 1, 1, sysdate(), 1);

-- 创建用户组角色关系表
drop table if exists `sys_user_group_role`;
create table sys_user_group_role
(
    group_id       bigint               not null comment '用户组ID',
    role_id        bigint               not null comment '角色ID',
    use_yn         tinyint(1) default 1 not null comment '是否启用',
    create_time    datetime             not null comment '创建时间',
    create_user_id bigint               null comment '创建人员ID',
    primary key (group_id, role_id)
);

-- 创建角色功能关系表
drop table if exists `sys_role_function`;
create table sys_role_function
(
    role_id        bigint   not null,
    function_id    bigint   not null,
    create_time    datetime not null,
    create_user_id bigint   null,
    primary key (role_id, function_id)
)
    comment '角色菜单关系表';

INSERT INTO sys_role_function (role_id, function_id, create_time, create_user_id) VALUES (1, 2, sysdate(), 1);
INSERT INTO sys_role_function (role_id, function_id, create_time, create_user_id) VALUES (1, 21, sysdate(), 1);
INSERT INTO sys_role_function (role_id, function_id, create_time, create_user_id) VALUES (1, 22, sysdate(), 1);
INSERT INTO sys_role_function (role_id, function_id, create_time, create_user_id) VALUES (1, 23, sysdate(), 1);
INSERT INTO sys_role_function (role_id, function_id, create_time, create_user_id) VALUES (1, 24, sysdate(), 1);
INSERT INTO sys_role_function (role_id, function_id, create_time, create_user_id) VALUES (1, 25, sysdate(), 1);

-- 创建国际化组表
drop table if exists `sys_i18n_group`;
create table sys_i18n_group
(
    group_id       bigint       not null
        primary key,
    group_name     varchar(255) not null,
    create_time    datetime     not null,
    create_user_id bigint       null,
    update_time    datetime     null,
    update_user_id bigint       null,
    seq            int          not null
)
    comment '国家化分组';
INSERT INTO sys_i18n_group (group_id, group_name, create_time, create_user_id, update_time, update_user_id, seq) VALUES (1449074421137442, '系统认证', sysdate(), 1, null, null, 2);
INSERT INTO sys_i18n_group (group_id, group_name, create_time, create_user_id, update_time, update_user_id, seq) VALUES (1450875996340258, '系统菜单', sysdate(), 1, null, null, 100);
INSERT INTO sys_i18n_group (group_id, group_name, create_time, create_user_id, update_time, update_user_id, seq) VALUES (1453367953981474, '系统验证消息', sysdate(), 1, null, 1, 200);
INSERT INTO sys_i18n_group (group_id, group_name, create_time, create_user_id, update_time, update_user_id, seq) VALUES (1472568575918114, '系统模块国际化', sysdate(), 1, null, null, 300);

-- 创建国际化信息表
drop table if exists `sys_i18n`;
create table sys_i18n
(
    i18n_id        bigint               not null
        primary key,
    platform       varchar(50)          not null comment '平台',
    i18n_code      varchar(255)         not null comment '国际化编码',
    group_id       bigint               not null comment '国际化分组ID',
    remark         varchar(200)         null comment '备注',
    create_user_id bigint               null,
    update_time    datetime             null,
    update_user_id bigint               null,
    use_yn         tinyint(1) default 1 null,
    delete_yn      tinyint(1) default 0 null,
    seq            int                  not null,
    create_time    datetime             not null,
    constraint uni_i18n_platform_code
        unique (platform, i18n_code)
)
    comment '国际化信息';

INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1449074511314978, 'backstage', 'auth.error.tempToken.empty', 1449074421137442, null, 1, null, null, 1, 0, 1, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1449074565840930, 'backstage', 'auth.error.tempToken.expire', 1449074421137442, null, 1, null, null, 1, 0, 2, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1449074584715298, 'backstage', 'auth.error.tempToken.ipFail', 1449074421137442, null, 1, null, null, 1, 0, 3, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1449074605686818, 'backstage', 'auth.error.tempToken.resourceFail', 1449074421137442, null, 1, null, null, 1, 0, 4, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1449074626658338, 'backstage', 'auth.error.tempToken.apply.resourceNull', 1449074421137442, null, 1, null, null, 1, 0, 6, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1449074649727010, 'backstage', 'auth.error.tempToken.apply.resourceFail', 1449074421137442, null, 1, null, null, 1, 0, 7, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1449075132071970, 'backstage', 'auth.error.usernamePasswordError', 1449074421137442, null, 1, null, null, 1, 0, 8, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1449075150946338, 'backstage', 'auth.error.usernamePasswordEmpty', 1449074421137442, null, 1, null, null, 1, 0, 9, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1449075169820706, 'backstage', 'auth.error.phoneCodeEmpty', 1449074421137442, null, 1, null, null, 1, 0, 10, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1449075190792226, 'backstage', 'auth.error.phoneCodeError', 1449074421137442, null, 1, null, null, 1, 0, 11, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1449075213860898, 'backstage', 'auth.error.phoneEmpty', 1449074421137442, null, 1, null, null, 1, 0, 12, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1449075234832418, 'backstage', 'auth.error.token.expire', 1449074421137442, null, 1, null, null, 1, 0, 13, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1449075262095394, 'backstage', 'auth.error.ip.validate', 1449074421137442, null, 1, null, null, 1, 0, 14, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1449075706691618, 'backstage', 'auth.error.changePassword.oldPasswordError', 1449074421137442, null, 1, null, null, 1, 0, 15, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1449075727663138, 'backstage', 'auth.error.changePassword.passwordInconsistent', 1449074421137442, null, 1, null, null, 1, 0, 16, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1450876019408930, 'backstage', 'menu.system.system', 1450875996340258, null, 1, null, null, 1, 0, 10, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1450876044574754, 'backstage', 'menu.system.user.manager', 1450875996340258, null, 1, null, null, 1, 0, 20, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1450876608708642, 'backstage', 'menu.system.role.manager', 1450875996340258, null, 1, null, null, 1, 0, 30, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1450876638068770, 'backstage', 'menu.system.function.manager', 1450875996340258, null, 1, null, null, 1, 0, 40, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1450876661137442, 'backstage', 'menu.system.userGroup.manager', 1450875996340258, null, 1, null, null, 1, 0, 50, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1450876688400418, 'backstage', 'menu.system.i18n.manager', 1450875996340258, null, 1, null, null, 1, 0, 60, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1453368287428642, 'backstage', 'message.validate.useYn.idList.notNull', 1453367953981474, null, 1, null, null, 1, 0, 10, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1453368310497314, 'backstage', 'message.validate.useYn.status.notNull', 1453367953981474, null, 1, null, null, 1, 0, 20, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1455943906754594, 'backstage', 'auth.error.userNotFoundError', 1449074421137442, null, 1, null, null, 1, 0, 17, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1458478197506082, 'backstage', 'auth.error.token.empty', 1449074421137442, '用户未登录', 1, null, null, 1, 0, 20, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1459185399103522, 'backstage', 'menu.monitor.main', 1450875996340258, '菜单-系统监控', 1, '2022-01-18 12:03:50', 1, 1, 0, 200, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1459185510252578, 'backstage', 'menu.monitor.onlineUser', 1450875996340258, '菜单-系统监控-在线用户', 1, null, null, 1, 0, 210, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1459219880476706, 'backstage', 'message.system.validate.username.notNull', 1453367953981474, '认证消息-用户名不能为空', 1, null, null, 1, 0, 30, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1459556360126498, 'backstage', 'menu.monitor.sysLog', 1450875996340258, '菜单-系统监控-系统日志', 1, null, null, 1, 0, 220, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1461165987201058, 'backstage', 'menu.system.dataDict.manager', 1450875996340258, '系统菜单-数据字典', 1, null, null, 1, 0, 230, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1472568840159266, 'backstage', 'system.i18n.code.duplicate', 1472568575918114, '系统模块-国际化页面-国际化编码冲突', 1, null, null, 1, 0, 10, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1472568963891234, 'backstage', 'system.i18n.item.duplicate', 1472568575918114, '系统模块-国际化页面-语言冲突', 1, null, null, 1, 0, 20, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1472569530122274, 'backstage', 'system.user.account.exist.error', 1472568575918114, '系统模块-用户管理-账户重复创建错误', 1, null, null, 1, 0, 30, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1472584421998626, 'backstage', 'system.user.account.delete', 1472568575918114, '系统模块-用户管理-已删除用户创建账户', 1, null, null, 1, 0, 40, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1472584566702114, 'backstage', 'system.user.account.noUse', 1472568575918114, '系统模块-用户管理-已停用用户创建账户', 1, null, null, 1, 0, 50, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1472616231600162, 'backstage', 'auth.check.account.notCreated', 1449074421137442, '账户未创建', 1, null, null, 1, 0, 21, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1472616349040674, 'backstage', 'auth.check.noLogin.locked', 1449074421137442, '长时间未登录，账户锁定', 1, null, null, 1, 0, 22, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1472616445509666, 'backstage', 'auth.check.password.noModify.locked', 1449074421137442, '长时间未修改密码，账户锁定', 1, null, null, 1, 0, 23, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1472623118647330, 'backstage', 'auth.check.ip.whitelist.error', 1449074421137442, '登录IP不在白名单内', 1, null, null, 1, 0, 24, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1485798125862946, 'backstage', 'menu.monitor.exception', 1450875996340258, '系统-异常信息', 1, null, null, 1, 0, 240, sysdate());

-- 创建国际化项表
drop table if exists `sys_i18n_item`;
create table sys_i18n_item
(
    i18n_item_id   bigint               not null
        primary key,
    locale         varchar(50)          not null comment '语言',
    value          varchar(500)         not null comment '国际化内容',
    create_time    datetime             not null,
    create_user_id bigint               not null,
    use_yn         tinyint(1) default 1 null,
    delete_yn      tinyint(1) default 0 null,
    i18n_id        bigint               not null,
    constraint uni_sys_i18n_item_local
        unique (i18n_id, locale)
)
    comment '国家化项信息';

INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449074733613090, 'en-US', 'Temp Token validate fail，token is empty', sysdate(), 1, 1, 0, 1449074511314978);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449074708447266, 'zh-CN', '临时token验证失败：token不能为空', sysdate(), 1, 1, 0, 1449074511314978);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449074754584610, 'en-US', 'Temp Token validate fail，token is expire', sysdate(), 1, 1, 0, 1449074565840930);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449074800721954, 'zh-CN', '临时token验证失败，token已过期', sysdate(), 1, 1, 0, 1449074565840930);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449074830082082, 'en-US', 'Temp Token validate fail： IP validate fail', sysdate(), 1, 1, 0, 1449074584715298);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449074853150754, 'zh-CN', '临时token验证失败，IP验证失败', sysdate(), 1, 1, 0, 1449074584715298);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449074874122274, 'en-US', 'Temp Token validate fail: resource validate fail', sysdate(), 1, 1, 0, 1449074605686818);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449074905579554, 'zh-CN', '临时token验证失败，资源验证失败', sysdate(), 1, 1, 0, 1449074605686818);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449074947522594, 'en-US', 'Temp Token apply fail, resource is empty', sysdate(), 1, 1, 0, 1449074626658338);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449074974785570, 'zh-CN', '临时token申请失败：资源为空', sysdate(), 1, 1, 0, 1449074626658338);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449075020922914, 'en-US', 'Temp Token apply fail, resource validate fail', sysdate(), 1, 1, 0, 1449074649727010);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449075012534306, 'zh-CN', '临时token申请失败，资源验证失败', sysdate(), 1, 1, 0, 1449074649727010);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449075358564386, 'en-US', 'username or password error, login fail [{0}] time', sysdate(), 1, 1, 0, 1449075132071970);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449075348078626, 'zh-CN', '用户名密码错误，登录失败【{0}】次', sysdate(), 1, 1, 0, 1449075132071970);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449075396313122, 'en-US', 'username and password can not empty', sysdate(), 1, 1, 0, 1449075150946338);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449075390021666, 'zh-CN', '用户名密码不能为空', sysdate(), 1, 1, 0, 1449075150946338);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449075452936226, 'en-US', 'phone and code can not empty', sysdate(), 1, 1, 0, 1449075169820706);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449075442450466, 'zh-CN', '手机验证码不能为空', sysdate(), 1, 1, 0, 1449075169820706);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449075503267874, 'en-US', 'phone or code error', sysdate(), 1, 1, 0, 1449075190792226);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449075494879266, 'zh-CN', '手机号或验证码错误', sysdate(), 1, 1, 0, 1449075190792226);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449075545210914, 'en-US', 'phone can not empty', sysdate(), 1, 1, 0, 1449075213860898);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449075538919458, 'zh-CN', '手机号不能为空', sysdate(), 1, 1, 0, 1449075213860898);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449075603931170, 'en-US', 'login expire, please login in again', sysdate(), 1, 1, 0, 1449075234832418);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449075585056802, 'zh-CN', '登录过期请重新登录', sysdate(), 1, 1, 0, 1449075234832418);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449075660554274, 'en-US', 'The login IP is different from the access IP', sysdate(), 1, 1, 0, 1449075262095394);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449075652165666, 'zh-CN', '访问IP与登录IP不同', sysdate(), 1, 1, 0, 1449075262095394);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449075782189090, 'en-US', 'old password error', sysdate(), 1, 1, 0, 1449075706691618);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449075765411874, 'zh-CN', '修改密码失败：原密码错误', sysdate(), 1, 1, 0, 1449075706691618);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449075855589410, 'en-US', 'The password is inconsistent', sysdate(), 1, 1, 0, 1449075727663138);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1449075824132130, 'zh-CN', '修改密码失败：密码不一致', sysdate(), 1, 1, 0, 1449075727663138);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1450876753412130, 'en-US', 'System Manager', sysdate(), 1, 1, 0, 1450876019408930);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1450876715663394, 'zh-CN', '系统管理', sysdate(), 1, 1, 0, 1450876019408930);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1450876810035234, 'en-US', 'User Manager', sysdate(), 1, 1, 0, 1450876044574754);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1450876789063714, 'zh-CN', '用户管理', sysdate(), 1, 1, 0, 1450876044574754);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1450877065887778, 'en-US', 'Role Manager', sysdate(), 1, 1, 0, 1450876608708642);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1450877049110562, 'zh-CN', '角色管理', sysdate(), 1, 1, 0, 1450876608708642);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1450877112025122, 'en-US', 'Function Manager', sysdate(), 1, 1, 0, 1450876638068770);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1450877093150754, 'zh-CN', '功能管理', sysdate(), 1, 1, 0, 1450876638068770);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1450877158162466, 'en-US', 'User Group Manager', sysdate(), 1, 1, 0, 1450876661137442);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1450877132996642, 'zh-CN', '用户组管理', sysdate(), 1, 1, 0, 1450876661137442);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1455944166801442, 'en-US', 'I18N Manager', sysdate(), 1, 1, 0, 1450876688400418);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1455944103886882, 'zh-CN', '国际化管理', sysdate(), 1, 1, 0, 1450876688400418);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1453368434229282, 'en-US', 'ID cannot be empty', sysdate(), 1, 1, 0, 1453368287428642);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1453368346148898, 'zh-CN', 'ID不能为空', sysdate(), 1, 1, 0, 1453368287428642);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1453368524406818, 'en-US', 'Start stop status cannot be empty', sysdate(), 1, 1, 0, 1453368310497314);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1453368488755234, 'zh-CN', '启停状态不能为空', sysdate(), 1, 1, 0, 1453368310497314);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1455943978057762, 'en-US', 'user is not found', sysdate(), 1, 1, 0, 1455943906754594);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1455943963377698, 'zh-CN', '用户不存在', sysdate(), 1, 1, 0, 1455943906754594);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1458478283489314, 'en-US', 'not login, please login', sysdate(), 1, 1, 0, 1458478197506082);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1458478262517794, 'zh-CN', '请登录', sysdate(), 1, 1, 0, 1458478197506082);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1459185455726626, 'en-US', 'System Monitor', sysdate(), 1, 1, 0, 1459185399103522);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1459185441046562, 'zh-CN', '系统监控', sysdate(), 1, 1, 0, 1459185399103522);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1459193473138722, 'en-US', 'Online User', sysdate(), 1, 1, 0, 1459185510252578);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1459193443778594, 'zh-CN', '在线用户', sysdate(), 1, 1, 0, 1459185510252578);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1459219951779874, 'en-US', 'Username cannot be empty', sysdate(), 1, 1, 0, 1459219880476706);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1459219907739682, 'zh-CN', '用户名不能为空', sysdate(), 1, 1, 0, 1459219880476706);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1459556399972386, 'en-US', 'System Log', sysdate(), 1, 1, 0, 1459556360126498);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1459556383195170, 'zh-CN', '系统日志', sysdate(), 1, 1, 0, 1459556360126498);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1461166022852642, 'en-US', 'Data Dict', sysdate(), 1, 1, 0, 1461165987201058);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1461166006075426, 'zh-CN', '数据字典', sysdate(), 1, 1, 0, 1461165987201058);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1472568898879522, 'en-US', 'I18N Code Duplicate, key: {0}', sysdate(), 1, 1, 0, 1472568840159266);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1472568882102306, 'zh-CN', '国际化编码冲突，key：{0}', sysdate(), 1, 1, 0, 1472568840159266);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1472568984862754, 'en-US', 'I18N Item Locale Duplicate, key: {0}', sysdate(), 1, 1, 0, 1472568963891234);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1472569016320034, 'zh-CN', '国际化语言冲突, key: {0}', sysdate(), 1, 1, 0, 1472568963891234);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1472569561579554, 'en-US', 'Account cannot be created repeatedly, user: {0}', sysdate(), 1, 1, 0, 1472569530122274);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1472569620299810, 'zh-CN', '不能重复创建账户，用户：{0}', sysdate(), 1, 1, 0, 1472569530122274);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1472584512176162, 'en-US', 'Deleted user cannot create account, deleted user: {0}', sysdate(), 1, 1, 0, 1472584421998626);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1472584491204642, 'zh-CN', '已删除用户不能创建账户，以删除用户：{0}', sysdate(), 1, 1, 0, 1472584421998626);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1472584612839458, 'en-US', 'Disabled user cannot create account, disabled user: {0}', sysdate(), 1, 1, 0, 1472584566702114);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1484907689803810, 'zh-CN', '已停用用户不能创建账户：停用用户：{0}', sysdate(), 1, 1, 0, 1472584566702114);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1472616279834658, 'en-US', 'Account not created', sysdate(), 1, 1, 0, 1472616231600162);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1472616271446050, 'zh-CN', '账户未创建', sysdate(), 1, 1, 0, 1472616231600162);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1472616403566626, 'en-US', 'Account locking without login for a long time', sysdate(), 1, 1, 0, 1472616349040674);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1472616361623586, 'zh-CN', '长时间未登录，账户锁定', sysdate(), 1, 1, 0, 1472616349040674);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1472616476966946, 'en-US', 'Account lock without password modification', sysdate(), 1, 1, 0, 1472616445509666);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1472616460189730, 'zh-CN', '长时间未修改密码，账户锁定', sysdate(), 1, 1, 0, 1472616445509666);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1472623171076130, 'en-US', 'Login IP is not in the white list', sysdate(), 1, 1, 0, 1472623118647330);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1472623150104610, 'zh-CN', '登录IP不在白名单内', sysdate(), 1, 1, 0, 1472623118647330);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1485798178291746, 'en-US', 'System Exception', sysdate(), 1, 1, 0, 1485798125862946);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1485798146834466, 'zh-CN', '异常信息', sysdate(), 1, 1, 0, 1485798125862946);


-- 创建系统日志表
drop table if exists `sys_log`;
create table sys_log
(
    log_id         bigint        not null
        primary key,
    operation      varchar(255)  null comment '操作',
    use_time       bigint        null comment '用时',
    method         varchar(1000) null comment '请求类函数',
    params         text          null comment '参数',
    ip             varchar(50)   null comment 'IP地址',
    request_path   varchar(255)  null comment '请求路径',
    status_code    int           not null comment '状态码',
    error_message  varchar(1000) null comment '错误信息',
    result         text          null comment '结果',
    operation_type varchar(50)   null comment '操作类型：
// 增加
ADD,
 // 删除
 DELETE,
 // 修改
 UPDATE,
 // 查询
 QUERY',
    platform       varchar(100)  null comment '平台',
    log_source     char(2)       not null comment '来源：
10：系统自动
20：手动日志
30：登录日志
40：登出日志
50：登录失败日志',
    create_time    datetime      not null,
    create_user_id bigint        null
);

-- 创建系统文件信息表
drop table if exists `sys_file`;
create table sys_file
(
    file_id        bigint       not null comment '文件ID'
        primary key,
    file_name      varchar(255) null comment '文件名',
    create_time    datetime     not null comment '创建时间',
    create_user_id bigint       null comment '创建人员ID',
    type           varchar(255) null comment '类型',
    content_type   varchar(255) not null comment '文件类型',
    file_size      bigint       not null comment '文件大小',
    db_id          varchar(255) not null comment '数据库ID',
    md5            varchar(256) not null comment 'MD5',
    seq            int          null comment '序号',
    handler_type   varchar(20)  not null comment '默认的文件执行器'
);

-- 创建系统异常信息表
drop table if exists `sys_exception`;
create table sys_exception
(
    id                bigint               not null primary key,
    exception_message text                 not null,
    stack_trace       text                 not null comment '异常堆栈信息',
    request_ip        varchar(50)          null comment '请求IP',
    server_ip         varchar(50)          not null comment '服务器IP',
    request_path      varchar(500)         null comment '请求路径',
    operate_user_id   bigint               null comment '操作人员ID',
    create_time       datetime             not null comment '创建时间',
    user_feedback     tinyint(1) default 0 not null comment '用户是否反馈',
    feedback_message  varchar(500)         null comment '用户反馈信息',
    feedback_time     datetime             null comment '反馈时间',
    resolved          tinyint(1) default 0 not null comment '是否已处理',
    resolved_message  varchar(500)         null comment '处理信息',
    resolved_user_id  bigint               null comment '处理人员ID',
    resolved_time     datetime             null comment '处理时间'
)
    comment '系统异常信息';


-- 创建字典表
drop table if exists `sys_dict`;
create table sys_dict
(
    dict_code      varchar(255)         not null comment '字典编码'
        primary key,
    dict_name      varchar(255)         not null comment '字典名称',
    seq            int                  not null comment '序号',
    use_yn         tinyint(1) default 1 not null,
    delete_yn      tinyint(1) default 0 not null,
    create_user_id bigint               not null,
    create_time    datetime             not null,
    update_user_id bigint               null,
    update_time    datetime             null
)
    comment '系统字典表';

-- 创建字典项表
drop table if exists `sys_dict_item`;
create table sys_dict_item
(
    dict_code      varchar(255)         not null comment '字典编码',
    dict_item_code varchar(255)         not null comment '字典项编码',
    dict_item_name varchar(255)         not null comment '字典项名称',
    seq            int                  not null comment '序号',
    remark         varchar(255)         null comment '描述',
    use_yn         tinyint(1) default 1 not null,
    delete_yn      tinyint    default 0 not null,
    create_user_id bigint               not null,
    create_time    datetime             not null,
    update_user_id bigint               null,
    update_time    datetime             null,
    primary key (dict_code, dict_item_code)
)
    comment '字典序表';

-- 菜单访问记录表
drop table if exists `sys_menu_access_log`;
create table sys_menu_access_log
(
    log_id         bigint   not null
        primary key,
    function_id    bigint   not null,
    create_time    datetime not null,
    create_user_id bigint   null
)
    comment '菜单访问记录表';

create index idx_sys_menu_access_log_function_id
    on sys_menu_access_log (function_id);

create index idx_sys_menu_access_log_user_id
    on sys_menu_access_log (create_user_id);


