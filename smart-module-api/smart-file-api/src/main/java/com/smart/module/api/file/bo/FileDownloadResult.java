package com.smart.module.api.file.bo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

/**
 * 文件下载结果
 * @author zhongming4762
 * 2023/2/16
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class FileDownloadResult extends FileHandlerResult {

    private transient InputStream inputStream;
}
