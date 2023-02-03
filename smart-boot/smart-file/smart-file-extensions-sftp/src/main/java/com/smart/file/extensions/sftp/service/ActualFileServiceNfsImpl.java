package com.smart.file.extensions.sftp.service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.smart.file.core.SmartFileProperties;
import com.smart.file.core.common.ActualFileServiceRegisterName;
import com.smart.file.core.constants.ActualFileServiceEnum;
import com.smart.file.core.exception.SmartFileException;
import com.smart.file.core.model.FileSaveParameter;
import com.smart.file.core.pojo.bo.DiskFilePathBO;
import com.smart.file.core.service.ActualFileService;
import com.smart.file.extensions.sftp.exception.SftpExceptionRuntimeException;
import com.smart.file.extensions.sftp.provider.JschChannelProvider;
import com.smart.file.extensions.sftp.utils.JschUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.io.*;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2020/12/7 14:56
 * @since 1.0
 */
public class ActualFileServiceNfsImpl implements ActualFileService {

    private final JschChannelProvider<ChannelSftp> jcraftChannelProvider;

    private final String basePath;

    public ActualFileServiceNfsImpl(JschChannelProvider<ChannelSftp> jcraftChannelProvider, SmartFileProperties properties) {
        this.jcraftChannelProvider = jcraftChannelProvider;
        if (StringUtils.isBlank(properties.getSftp().getBasePath())) {
            throw new SmartFileException("使用NFS必须设置基础路径：smart:file:nfs:base-path");
        }
        this.basePath = properties.getSftp().getBasePath();
    }

    /**
     * 保存文件
     * @param file 文件
     * @param parameter 参数
     * @return 文件id
     */
    @Override
    @NonNull
    public String save(@NonNull File file, @NonNull FileSaveParameter parameter) throws IOException {
        try (
                FileInputStream inputStream = new FileInputStream(file);
                ) {
            return this.save(inputStream, parameter);
        }
    }


    /**
     * 保存文件
     * 计算文件MD5会造成非常大的内容压力
     * @param inputStream 文件流
     * @param parameter 参数
     * @return 文件ID
     */
    @SneakyThrows(SftpException.class)
    @Override
    @NonNull
    public String save(@NonNull InputStream inputStream, @NonNull FileSaveParameter parameter) {
        // 获取channel
        final ChannelSftp channelSftp = this.jcraftChannelProvider.getChannel();
        try {
            final DiskFilePathBO diskFilePath = new DiskFilePathBO(this.basePath, parameter);
            // 创建并进入路径
            JschUtils.createDirectories(channelSftp, diskFilePath.getFolderPath());
            // 执行保存
            channelSftp.put(inputStream, diskFilePath.getDiskFilename());
            return diskFilePath.getFileId();
        } finally {
            // 归还连接
            this.jcraftChannelProvider.returnChannel(channelSftp);
        }
    }

    /**
     * 删除文件
     * @param id 文件ID
     */
    @Override
    public void delete(@NonNull String id) {
        // 获取channel
        final ChannelSftp channelSftp = this.jcraftChannelProvider.getChannel();
        try {
            final DiskFilePathBO diskFile = DiskFilePathBO.createById(id, this.basePath);
            channelSftp.cd(diskFile.getFolderPath());
            channelSftp.rm(diskFile.getDiskFilename());
        } catch (SftpException e) {
            throw new SftpExceptionRuntimeException(e);
        } finally {
            // 归还连接
            this.jcraftChannelProvider.returnChannel(channelSftp);
        }
    }

    /**
     * 批量删除文件
     * @param fileIdList 文件ID
     */
    @Override
    public void batchDelete(@NonNull List<String> fileIdList) {
        fileIdList.forEach(this::delete);
    }

    /**
     * 下载文件
     * @param id 文件id
     * @return 文件流
     */
    @Override
    public InputStream download(@NonNull String id) {
        throw new UnsupportedOperationException("NFS方式不支持该方式下载文件，请通过download(String id, OutputStream outputStream)下载文件");
    }

    /**
     * 下载文件
     * @param id 文件ID
     * @param outputStream 输出流，文件信息会写入输出流
     */
    @SneakyThrows({SftpException.class, IOException.class})
    @Override
    public void download(@NonNull String id, @NonNull OutputStream outputStream) {
        // 获取channel
        final ChannelSftp channelSftp = this.jcraftChannelProvider.getChannel();
        try {
            final DiskFilePathBO diskFile = DiskFilePathBO.createById(id, this.basePath);
            channelSftp.cd(diskFile.getFolderPath());
            try (final InputStream inputStream = channelSftp.get(diskFile.getDiskFilename())) {
                IOUtils.copy(inputStream, outputStream);
            }
        } finally {
            // 归还连接
            this.jcraftChannelProvider.returnChannel(channelSftp);
        }
    }

    /**
     * 获取文件的绝对路径
     * @param id 文件ID
     * @return 文件绝对路径
     */
    @Override
    public String getAbsolutePath(@NonNull String id) {
        return DiskFilePathBO.createById(id, this.basePath).getFilePath();
    }

    /**
     * 获取注册名字
     * @return 注册名字
     */
    @Override
    public ActualFileServiceRegisterName getRegisterName() {
        return ActualFileServiceRegisterName.builder()
                .dbName(ActualFileServiceEnum.SFTP.name())
                .beanName(ActualFileServiceEnum.SFTP.getServiceName())
                .build();
    }
}
