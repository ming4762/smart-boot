package com.smart.file.manager.service;

import com.smart.crud.service.BaseService;
import com.smart.file.manager.model.SysFilePO;

/**
 * 文件服务层
 * @author shizhongming
 * 2020/1/27 7:50 下午
 */
public interface SysFileService extends BaseService<SysFilePO> {

<<<<<<< HEAD
    /**
     * 保存文件
     * @param multipartFile 文件信息
     * @param saveFileDTO   文件信息
     * @return 文件实体
     * @throws IOException 异常信息
     */
    @NonNull
    SysFilePO saveFile(@NonNull MultipartFile multipartFile, @NonNull SaveFileDTO saveFileDTO) throws IOException;

    /**
     * 保存文件
     *
     * @param file 文件信息
     * @return 文件信息
     * @throws IOException 异常信息
     */
    @NonNull
    SysFilePO saveFile(@NonNull SysFileBO file) throws IOException;

    /**
     * 保存文件
     *
     * @param multipartFile 文件信息
     * @param type          文件类型
     * @param handlerType   指定文件执行器类型
     * @return 文件实体信息
     */
    SysFilePO saveFile(@NonNull MultipartFile multipartFile, String type, String handlerType);

    /**
     * 保存文件
     *
     * @param multipartFile 文件信息
     * @param type          文件类型
     * @return 文件实体信息
     */
    SysFilePO saveFile(@NonNull MultipartFile multipartFile, String type);

    /**
     * 获取相同的file
     * @param file 比对的文件信息
     * @return 相同的文件信息，返回null 则不存在相同的文件
     */
    @Nullable
    SysFilePO getSameFile(@NonNull SysFilePO file);

    /**
     * 删除文件
     *
     * @param fileId 文件ID
     * @return 文件信息
     * @throws IOException IOException
     */
    @Nullable
    SysFilePO deleteFile(@NonNull Long fileId) throws IOException;

    /**
     * 批量删除文件
     *
     * @param fileIds 文件id列表
     * @return 删除是否成功
     * @throws IOException IOException
     */
    boolean batchDeleteFile(@NonNull Collection<Serializable> fileIds) throws IOException;

    /**
     * 下载文件
     *
     * @param fileId 文件ID
     * @return 文件信息
     */
    SysFileBO download(@NonNull Long fileId);

    /**
     * 下载文件
     *
     * @param file 文件实体类信息
     * @return 文件信息
     */
    SysFileBO download(@NonNull SysFilePO file);

    /**
     * 获取文件的绝对路径
     *
     * @param fileId 文件ID
     * @return 文件绝对路径
     */
    String getAbsolutePath(@NonNull Long fileId);
=======
>>>>>>> 22d0df4 (文件管理模块重构，优化使用体验)
}
