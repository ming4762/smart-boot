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
    /**
     * 通过代码查询文件存储器
     */
    String FILE_STORAGE_LIST_BY_CODE = "/remote/file/storage/listByCode";

    String DOWNLOAD_FILE = "/remote/file/storage/download";
     /**
     * 通过文件名下载文件
     */
    String DOWNLOAD_FILE_BY_NAME = "/remote/file/storage/downloadByName";

    String BATCH_DELETE = "/remote/file/storage/batchDelete";

    String SAVE = "/remote/file/storage/save";

    String DELETE = "/remote/file/storage/delete";

    String LIST_ADDRESS = "/remote/file/listAddress";
}
