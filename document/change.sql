-- 2025-04-09
-- 文件存储器支持加密文件
alter table smart_file_storage
    add encrypted_yn tinyint(1) default 0 not null comment '文件是否加密' after storage_config;

alter table smart_file_storage
    add private_key text null comment '加密文件私钥' after encrypted_yn;

alter table smart_file_storage
    add public_key text null comment '加密文件公钥' after private_key;

alter table smart_file
    add encrypted_yn tinyint(1) default 0 not null comment '文件是否加密，与存储器文件加密不同，这里可以单独指定每个文件是否加密，保证存储器这设置为加密之前的文件也可正常读取';



-- 20250906 系统参数租户表
create table sys_parameter_tenant
(
    id             bigint               not null
        primary key,
    parameter_id   bigint               not null,
    tenant_id      bigint               not null comment '租户id, -1则是通用值',
    parameter      varchar(2000)        not null comment '参数值',
    create_time    datetime             not null,
    create_by      varchar(200)         not null,
    create_user_id bigint               null,
    update_time    datetime             null,
    update_by      varchar(200)         null,
    update_user_id bigint               null
)
    comment '系统参数租户表';
create index idx_sys_parameter_tenant_parameter_id
    on sys_parameter_tenant (parameter_id);
create index idx_sys_parameter_tenant_tenant_id
    on sys_parameter_tenant (tenant_id);

-- 删除sys_parameter的parameter字段，改为从sys_parameter_tenant获取
alter table sys_parameter
    drop column parameter;