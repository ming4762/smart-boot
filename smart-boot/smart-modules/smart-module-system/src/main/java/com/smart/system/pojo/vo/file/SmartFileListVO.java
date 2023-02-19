package com.smart.system.pojo.vo.file;

import com.smart.system.model.file.SmartFilePO;
import com.smart.system.model.file.SmartFileStoragePO;
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
