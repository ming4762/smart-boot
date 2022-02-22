package com.smart.file.extensions.nfs.service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.smart.commons.core.exception.BaseException;
import com.smart.commons.core.utils.Md5Utils;
import com.smart.commons.file.SmartFileProperties;
import com.smart.commons.file.common.ActualFileServiceRegisterName;
import com.smart.commons.file.constants.ActualFileServiceEnum;
import com.smart.commons.file.pojo.bo.DiskFilePathBO;
import com.smart.commons.file.service.ActualFileService;
import com.smart.file.extensions.nfs.exception.SftpExceptionRuntimeException;
import com.smart.file.extensions.nfs.provider.JcraftChannelProvider;
import com.smart.file.extensions.nfs.utils.JcraftUtils;
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

    private final JcraftChannelProvider<ChannelSftp> jcraftChannelProvider;

    private final String basePath;

    public ActualFileServiceNfsImpl(JcraftChannelProvider<ChannelSftp> jcraftChannelProvider, SmartFileProperties properties) {
        this.jcraftChannelProvider = jcraftChannelProvider;
        if (StringUtils.isBlank(properties.getNfs().getBasePath())) {
            throw new BaseException("使用NFS必须设置基础路径：gc:file:nfs:base-path");
        }
        this.basePath = properties.getNfs().getBasePath();
    }

    /**
     * 保存文件
     * @param file 文件
     * @param filename 文件名
     * @return 文件id
     */
    @Override
    @NonNull
    public String save(@NonNull File file, String filename) throws IOException {
        try (
                FileInputStream inputStream = new FileInputStream(file);
                FileInputStream md5InputStream = new FileInputStream(file)
                ) {
            String md5 = Md5Utils.md5(md5InputStream);
            return this.save(inputStream, StringUtils.isEmpty(filename) ? file.getName() : filename, md5);
        }
    }


    /**
     * 保存文件
     * 计算文件MD5会造成非常大的内容压力
     * @param inputStream 文件流
     * @param filename 文件名
     * @return 文件ID
     */
    @SneakyThrows
    @Override
    @NonNull
    public String save(@NonNull InputStream inputStream, String filename, String md5) {
        // 获取channel
        final ChannelSftp channelSftp = this.jcraftChannelProvider.getChannel();
        // 计算md5
        final DiskFilePathBO diskFilePath = new DiskFilePathBO(this.basePath, md5, filename);
        // 创建并进入路径
        JcraftUtils.createDirectories(channelSftp, diskFilePath.getFolderPath());
        // 执行保存
        channelSftp.put(inputStream, diskFilePath.getDiskFilename());
        // 归还连接
        this.jcraftChannelProvider.returnChannel(channelSftp);
        return diskFilePath.getFileId();
    }

    /**
     * 删除文件
     * @param id 文件ID
     */
    @Override
    public void delete(@NonNull String id) {
        try {
            // 获取channel
            final ChannelSftp channelSftp = this.jcraftChannelProvider.getChannel();
            final DiskFilePathBO diskFile = DiskFilePathBO.createById(id, this.basePath);
            channelSftp.cd(diskFile.getFolderPath());
            channelSftp.rm(diskFile.getDiskFilename());
            // 归还连接
            this.jcraftChannelProvider.returnChannel(channelSftp);
        } catch (SftpException e) {
            throw new SftpExceptionRuntimeException(e);
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
    @SneakyThrows
    @Override
    public InputStream download(@NonNull String id) {
        // 获取channel
        final ChannelSftp channelSftp = this.jcraftChannelProvider.getChannel();
        final DiskFilePathBO diskFile = DiskFilePathBO.createById(id, this.basePath);
        channelSftp.cd(diskFile.getFolderPath());
        final ByteArrayInputStream byteArrayInputStream;
        try (
                final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                final InputStream inputStream = channelSftp.get(diskFile.getDiskFilename())
                ) {
            // 获取channel
            IOUtils.copy(inputStream, outputStream);
            byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        }
        // 归还连接
        this.jcraftChannelProvider.returnChannel(channelSftp);
        return byteArrayInputStream;
    }

    /**
     * 下载文件
     * @param id 文件ID
     * @param outputStream 输出流，文件信息会写入输出流
     */
    @SneakyThrows
    @Override
    public void download(@NonNull String id, @NonNull OutputStream outputStream) {
        // 获取channel
        final ChannelSftp channelSftp = this.jcraftChannelProvider.getChannel();
        final DiskFilePathBO diskFile = DiskFilePathBO.createById(id, this.basePath);
        channelSftp.cd(diskFile.getFolderPath());
        try (final InputStream inputStream = channelSftp.get(diskFile.getDiskFilename())) {
            IOUtils.copy(inputStream, outputStream);
        }
        // 归还连接
        this.jcraftChannelProvider.returnChannel(channelSftp);
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
                .dbName(ActualFileServiceEnum.NFS.name())
                .beanName(ActualFileServiceEnum.NFS.getServiceName())
                .build();
    }
}
