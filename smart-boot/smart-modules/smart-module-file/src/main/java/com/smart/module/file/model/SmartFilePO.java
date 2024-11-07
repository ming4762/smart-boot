package com.smart.module.file.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.annotation.TableLogicField;
import com.smart.framework.crud.model.BaseModelCreateUserTime;
import lombok.*;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author jackson
 * 2020/1/27 7:48 下午
 */
@TableName("smart_file")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SmartFilePO extends BaseModelCreateUserTime {

    @Serial
    private static final long serialVersionUID = -9077274336204793728L;

    /**
     * 文件ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long fileId;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 类型
     */
    private String type;

    /**
     * 文件类型
     */
    private String contentType;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 保存到文件存储器的KEY
     */
    private String storageStoreKey;

    /**
     * MD5
     */
    private String md5;

    /**
     * 序号
     */
    private Integer seq;

    /**
     * 文件存储器ID
     */
    private Long fileStorageId;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    @TableLogic
    private Boolean deleteYn;

    @TableLogicField(isDeleteKey = true)
    private Long deleteKey;
}
