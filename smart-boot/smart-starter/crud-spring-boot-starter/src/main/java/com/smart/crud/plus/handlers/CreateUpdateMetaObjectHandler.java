package com.smart.crud.plus.handlers;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.smart.crud.constants.UserPropertyEnum;
import com.smart.crud.service.UserProvider;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * @author shizhongming
 * 2024/4/19 20:53
 * @since 3.0.0
 */
@RequiredArgsConstructor
public class CreateUpdateMetaObjectHandler implements MetaObjectHandler {

    private final UserProvider userProvider;

    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, UserPropertyEnum.CREATE_USER_ID.getName(), this.userProvider::getCurrentUserId, Long.class);
        this.strictInsertFill(metaObject, UserPropertyEnum.CREATE_TIME.getName(), LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, UserPropertyEnum.CREATE_USER.getName(), this.userProvider::getCurrentUserFullName, String.class);
    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, UserPropertyEnum.UPDATE_USER_ID.getName(), this.userProvider::getCurrentUserId, Long.class);
        this.strictUpdateFill(metaObject, UserPropertyEnum.UPDATE_TIME.getName(), LocalDateTime::now, LocalDateTime.class);
        this.strictUpdateFill(metaObject, UserPropertyEnum.UPDATE_USER.getName(), this.userProvider::getCurrentUserFullName, String.class);
    }
}
