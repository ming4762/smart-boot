package com.smart.module.file.pojo.vo;

import com.smart.module.file.model.SmartFilePO;
import com.smart.module.file.model.SmartFileStoragePO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * @author zhongming4762
 * 2023/2/18
 */
@Getter
@Setter
@ToString
public class SmartFileListVO extends SmartFilePO {

    @Serial
    private static final long serialVersionUID = -155988800570412778L;

    private SmartFileStoragePO fileStorage;
}
