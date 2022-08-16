-- 插入菜单信息
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1463025647222818, 0, '应用监控', '10', '{menu.monitor.catalogue}', 500, sysdate(), 1, null, 1, null, null, 1, 0, 0, null, null, 'RadarChartOutlined');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1463026026807330, 1463025647222818, '应用管理', '20', '{menu.monitor.applicationManager}', 10, sysdate(), 1, null, 1, '/monitor/manager/application', null, 1, 0, 0, null, null, 'AppstoreOutlined');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1463207124271138, 1463025647222818, '客户端管理', '20', '{menu.monitor.clientManager}', 20, sysdate(), 1, null, null, '/monitor/manager/client', null, 1, 0, 0, null, null, 'BlockOutlined');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1464474382893090, 1463207124271138, '关闭客户端', '30', null, 10, sysdate(), 1, null, null, null, 'monitor:client:shutdown', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1464474487750690, 1463207124271138, '下载内存转储', '30', null, 20, sysdate(), 1, null, null, null, 'monitor:client:heapdump', 1, 0, 0, 'GET', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1464474640842786, 1463207124271138, '下载线程转储', '30', null, 30, sysdate(), 1, null, null, null, 'monitor:client:threaddump', 1, 0, 0, 'GET', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1464813077135394, 1463025647222818, '监控事件', '20', '{menu.monitor.event}', 50, '2022-02-18 13:28:25', 1, '2022-02-18 13:31:40', 1, '/monitor/manager/event', null, 1, 0, 0, null, null, 'DatabaseOutlined');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1465869280477218, 1463026026807330, '添加应用', '30', null, 10, sysdate(), 1, null, null, '/monitor/manager/application/saveUpdate', 'monitor:application:save', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1465869353877538, 1463026026807330, '修改应用', '30', null, 20, sysdate(), 1, null, null, '/monitor/manager/application/saveUpdate', 'monitor:application:update', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1465869471318050, 1463026026807330, '删除应用', '30', null, 30, sysdate(), 1, null, null, '/monitor/manager/application/batchDeleteById', 'monitor:application:delete', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1465869708296226, 1463026026807330, '设置应用关联用户组', '30', null, 40, sysdate(), 1, null, null, '/monitor/manager/application/setUserGroup', 'monitor:application:setUserGroup', 1, 0, 0, 'POST', null, null);
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1465884151382050, 1463025647222818, '慢SQL', '20', '{menu.monitor.slowSql}', 60, sysdate(), 1, null, null, '/monitor/manager/slowSql', null, 1, 0, 0, null, null, 'UnorderedListOutlined');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1467179648155682, 1463025647222818, '客户端日志', '20', '{menu.monitor.clientLog}', 70, sysdate(), 1, null, null, '/monitor/manager/log', null, 1, 0, 0, null, null, 'FileTextOutlined');
INSERT INTO sys_function (function_id, parent_id, function_name, function_type, i18n_code, seq, create_time, create_user_id, update_time, update_user_id, url, permission, is_menu, internal_or_external, data_rule, http_method, remark, icon) VALUES (1471178768121890, 1463025647222818, 'Http Trace', '20', null, 80, sysdate(), 1, null, null, '/monitor/manager/httpTrace', null, 1, 0, 0, null, null, 'MenuOutlined');

-- 国际化信息
INSERT INTO sys_i18n_group (group_id, group_name, create_time, create_user_id, update_time, update_user_id, seq) VALUES (1463025726914594, '监控模块菜单', sysdate(), 1, null, null, 160);

INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1463025785634850, 'backstage', 'menu.monitor.catalogue', 1463025726914594, '监控模块-主目录', 1, null, null, 1, 0, 10, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1463026075041826, 'backstage', 'menu.monitor.applicationManager', 1463025726914594, '应用管理', 1, null, null, 1, 0, 20, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1463207248003106, 'backstage', 'menu.monitor.clientManager', 1463025726914594, '菜单-应用监控-客户端管理', 1, null, null, 1, 0, 30, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1464813540605986, 'backstage', 'menu.monitor.event', 1463025726914594, '菜单-系统监控-监控事件', 1, null, null, 1, 0, 50, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1465884379971618, 'backstage', 'menu.monitor.slowSql', 1463025726914594, '菜单-应用监控-慢SQL', 1, null, null, 1, 0, 60, sysdate());
INSERT INTO sys_i18n (i18n_id, platform, i18n_code, group_id, remark, create_user_id, update_time, update_user_id, use_yn, delete_yn, seq, create_time) VALUES (1467179721556002, 'backstage', 'menu.monitor.clientLog', 1463025726914594, '应用监控-客户端日志', 1, null, null, 1, 0, 70, sysdate());

INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1463025852743714, 'en-US', 'App Monitor', sysdate(), 1, 1, 0, 1463025785634850);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1463025806606370, 'zh-CN', '应用监控', sysdate(), 1, 1, 0, 1463025785634850);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1463026119082018, 'en-US', 'App Manager', sysdate(), 1, 1, 0, 1463026075041826);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1463026091819042, 'zh-CN', '应用管理', sysdate(), 1, 1, 0, 1463026075041826);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1463207294140450, 'en-US', 'Client Manager', sysdate(), 1, 1, 0, 1463207248003106);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1463207266877474, 'zh-CN', '客户端管理', sysdate(), 1, 1, 0, 1463207248003106);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1464813580451874, 'en-US', 'Monitor Event', sysdate(), 1, 1, 0, 1464813540605986);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1464813559480354, 'zh-CN', '监控事件', sysdate(), 1, 1, 0, 1464813540605986);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1465884415623202, 'en-US', 'Slow SQL', sysdate(), 1, 1, 0, 1465884379971618);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1465884396748834, 'zh-CN', '慢SQL', sysdate(), 1, 1, 0, 1465884379971618);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1467179763499042, 'en-US', 'Client Log', sysdate(), 1, 1, 0, 1467179721556002);
INSERT INTO sys_i18n_item (i18n_item_id, locale, value, create_time, create_user_id, use_yn, delete_yn, i18n_id) VALUES (1467179740430370, 'zh-CN', '客户端日志', sysdate(), 1, 1, 0, 1467179721556002);


-- 应用信息表
drop table if exists `monitor_application`;
create table monitor_application
(
    id                   bigint               not null
        primary key,
    name                 varchar(255)         not null comment '应用名称',
    application_code     varchar(50)          not null comment '应用编码',
    remark               varchar(255)         null comment '备注',
    create_user_id       bigint               not null,
    update_user_id       bigint               null,
    create_time          datetime             not null,
    update_time          datetime             null,
    seq                  int                  not null comment '序号',
    use_yn               tinyint(1) default 1 not null comment '是否使用',
    delete_yn            tinyint(1) default 0 null comment '删除标识',
    status_interval      bigint               null comment '客户端状态检测间隔时间',
    offline_interval     bigint               null comment '客户端离线检测事件间隔',
    token                varchar(1000)        null comment '与客户端交互token',
    serialize_event_code varchar(1000)        null comment '序列化的事件编码列表',
    notify_event_code    varchar(1000)        null comment '通知的事件编码，以逗号分隔',
    notify_mails         varchar(2000)        null comment '通知邮箱，以 : 分割',
    constraint uni_monitor_application_application_code
        unique (application_code),
    constraint uni_monitor_application_name
        unique (name)
)
    comment '应用管理表';

-- http trace记录表
drop table if exists `monitor_client_http_trace`;
create table monitor_client_http_trace
(
    id               bigint        not null
        primary key,
    application_code varchar(255)  not null comment '客户端编码',
    client_id        varchar(255)  not null comment '客户端ID',
    create_time      datetime      not null,
    http_method      varchar(50)   not null comment '请求方式',
    time_taken       bigint        not null comment '用时(毫秒)',
    url              varchar(2000) not null comment '请求地址',
    response_status  int           not null comment '状态',
    timestamp        bigint        not null comment '时间戳',
    data             text          not null
)
    comment '客户端HttpTrace';

create index idx_monitor_client_trace_application_code
    on monitor_client_http_trace (application_code);

create index idx_monitor_client_trace_client_id
    on monitor_client_http_trace (client_id);

-- 日志表
drop table if exists `monitor_client_log`;
create table monitor_client_log
(
    id               bigint        not null
        primary key,
    application_code varchar(255)  not null comment '客户端编码',
    client_id        varchar(255)  not null comment '客户端ID',
    create_time      datetime      not null,
    thread_name      varchar(1000) not null comment '线程名',
    logger_name      varchar(1000) not null comment '日志名',
    timestamp        bigint        not null comment '时间戳',
    level            varchar(10)   not null comment '日志级别',
    log_text         text          not null comment '日志内容'
)
    comment '客户端日志';

create index idx_client_log_application_code
    on monitor_client_log (application_code);

create index idx_client_log_client_id
    on monitor_client_log (client_id);

create index idx_client_log_level
    on monitor_client_log (level);

create index idx_client_log_timestamp
    on monitor_client_log (timestamp);

-- slow sql记录表
drop table if exists `monitor_client_slow_sql`;
create table monitor_client_slow_sql
(
    id               bigint        not null
        primary key,
    application_code varchar(255)  not null comment '客户端编码
',
    client_id        varchar(255)  not null comment '客户端ID',
    client_url       varchar(1000) null comment '客户端地址',
    sql_text         text          not null comment 'SQL',
    parameter        text          null comment '参数',
    use_millis       bigint        not null comment '执行用时',
    datasource_name  varchar(255)  null comment '数据源名称',
    timestamp        bigint        not null comment '时间戳',
    sql_id           bigint        not null comment 'SQL ID'
)
    comment '客户端慢SQL';

create index idx_monitor_slow_sql_application_code
    on monitor_client_slow_sql (application_code);

create index idx_monitor_slow_sql_client_id
    on monitor_client_slow_sql (client_id);

-- 监控事件表
drop table if exists `monitor_event`;
create table monitor_event
(
    id               bigint       not null
        primary key,
    application_code varchar(255) not null comment '客户端编码',
    client_id        varchar(255) not null comment '客户端ID',
    event_code       varchar(255) not null comment '事件编码',
    timestamp        bigint       not null comment '时间戳',
    event_message    text         null comment '事件内容',
    create_time      datetime     not null
)
    comment '事件信息';

create index idx_monitor_event_application_code
    on monitor_event (application_code);

create index idx_monitor_event_client_id
    on monitor_event (client_id);

create index idx_monitor_event_event_code
    on monitor_event (event_code);

create index idx_monitor_event_timestamp
    on monitor_event (timestamp);

-- 应用用户组关系表
drop table if exists `monitor_user_group_application`;
create table monitor_user_group_application
(
    user_group_id  bigint   not null,
    application_id bigint   not null,
    create_user_id bigint   not null,
    create_time    datetime not null,
    primary key (user_group_id, application_id)
)
    comment '应用-用户组关系表';



