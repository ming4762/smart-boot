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
