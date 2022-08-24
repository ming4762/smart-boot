package com.smart.file.core.service;

import com.smart.file.core.common.ActualFileServiceRegisterName;
import org.springframework.lang.NonNull;

import java.io.*;
import java.util.List;

/**
 * 实际的文件执行器
 * @author zhongming
 */
public interface ActualFileService {

    /**
     * 保存文件
     * @param file 文件
     * @param filename 文件名
     * @throws IOException 文件写入失败抛出异常
     * @return 文件id
     */
    @NonNull
    String save(@NonNull File file, String filename) throws IOException;

    /**
     * 保存文件
     * @param inputStream 文件流
     * @param filename 文件名
     * @param md5 文件的MD5
     * @return 文件ID
     */
    @NonNull
    String save(@NonNull InputStream inputStream, String filename, String md5);

    /**
     * 删除文件
     * @param id 文件ID
     */
    void delete(@NonNull String id) ;


    /**
     * 批量删除文件
     * @param fileIdList 文件ID
     */
    void batchDelete(@NonNull List<String> fileIdList);

    /**
     * 下载文件
     * @param id 文件id
     * @throws FileNotFoundException 未找到下载文件错误
     * @return 文件流
     */
    InputStream download(@NonNull String id) throws FileNotFoundException;

    /**
     * 下载文件
     * @param id 文件ID
     * @param outputStream 输出流
     */
    void download(@NonNull String id, @NonNull OutputStream outputStream);

    /**
     * 获取文件的绝对路径
     * @param id 文件ID
     * @return 文件绝对路径
     */
    String getAbsolutePath(@NonNull String id);

    /**
     * 获取注册名字
     * @return 注册名字
     */
    ActualFileServiceRegisterName getRegisterName();

}
