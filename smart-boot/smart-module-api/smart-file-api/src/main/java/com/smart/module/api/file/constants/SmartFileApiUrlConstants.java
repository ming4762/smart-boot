package com.smart.module.api.file.constants;

/**
 * @author zhongming4762
 * 2023/3/21
 */
public interface SmartFileApiUrlConstants {

    /**
     * 通过ID查询文件存储器
     */
    String FILE_STORAGE_LIST_BY_ID = "/remote/file/storage/listById";

    String DOWNLOAD_FILE = "/remote/file/storage/download";

    String BATCH_DELETE = "/remote/file/storage/batchDelete";

    String SAVE = "/remote/file/storage/save";

    String DELETE = "/remote/file/storage/delete";
}
