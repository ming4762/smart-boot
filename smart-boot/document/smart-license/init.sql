
create table smart_auth_license
(
    id                bigint                   not null
        primary key,
    license_code      varchar(50)              not null comment 'license编码',
    project_id        bigint                   null comment '所属项目',
    version           varchar(50) default '1'  not null comment 'license版本',
    mac_address       varchar(1000)            null comment 'mac地址，多个mac地址以逗号分隔',
    ip_address        varchar(1000)            null comment '主板序列号，多个IP以逗号分隔',
    cpu_serial        varchar(255)             null comment 'cpu序列号',
    main_board_serial varchar(255)             null comment '主板序列号',
    effective_date    datetime                 not null comment '生效时间',
    expiration_time   datetime                 not null comment '过期时间',
    public_key        text                     null comment '公钥',
    private_key       text                     null comment '私钥',
    license           text                     null comment '许可证',
    status            char(2)     default '10' not null comment '10：创建，20：生成，license生成后不可修改',
    create_time       datetime                 not null,
    create_user_id    bigint                   null comment '创建人'
)
    comment '许可证管理';

create index idx_smart_auth_license_license
    on smart_auth_license (license_code);

