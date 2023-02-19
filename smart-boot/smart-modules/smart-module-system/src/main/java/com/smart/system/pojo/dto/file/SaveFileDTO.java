package com.smart.file.manager.pojo.dto;

import com.smart.file.manager.constants.FileTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author jackson
 * 2020/1/28 2:48 下午
 */
@Getter
@Setter
@Builder
@ToString
public class SaveFileDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8030356270886177531L;
    private String filename;

    private String folder;

    private FileTypeEnum type;

    private String handlerType;
}
