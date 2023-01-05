package com.smart.file.manager.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelCreateUserTime;
import com.smart.file.manager.constants.FileTypeEnum;
import lombok.*;

import java.io.Serial;

/**
 * @author jackson
 * 2020/1/27 7:48 下午
 */
@TableName("sys_file")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysFilePO extends BaseModelCreateUserTime {

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
    private String fileName;

    /**
     * 类型
     */
    private FileTypeEnum type;

    /**
     * 文件类型
     */
    private String contentType;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 数据库ID
     */
    private String dbId;

    /**
     * MD5
     */
    private String md5;

    /**
     * 序号
     */
    private Integer seq;

    /**
     * 文件执行器类型
     */
    private String handlerType;
}
