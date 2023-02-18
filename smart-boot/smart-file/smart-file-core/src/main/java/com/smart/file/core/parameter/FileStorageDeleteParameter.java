package com.smart.file.core.parameter;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 文件删除参数
 * @author zhongming4762
 * 2023/2/16
 */
@Getter
@Setter
@SuperBuilder
public class FileStorageDeleteParameter extends FileStorageCommonParameter {


    private List<String> fileStoreKeyList;
}
