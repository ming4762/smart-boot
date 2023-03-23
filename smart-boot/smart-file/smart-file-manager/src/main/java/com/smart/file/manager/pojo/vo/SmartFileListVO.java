package com.smart.file.manager.pojo.vo;

import com.smart.file.manager.model.SmartFilePO;
import com.smart.file.manager.model.SmartFileStoragePO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhongming4762
 * 2023/2/18
 */
@Getter
@Setter
@ToString
public class SmartFileListVO extends SmartFilePO {

    private SmartFileStoragePO fileStorage;
}
