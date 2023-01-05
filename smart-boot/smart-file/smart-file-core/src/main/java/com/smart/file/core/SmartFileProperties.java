package com.smart.file.core;

import com.smart.file.core.constants.ActualFileServiceEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文件服务层配置类
 * @author shizhongming
 * 2020/11/29 3:24 上午
 */
@ConfigurationProperties(prefix = "smart.file")
@Getter
@Setter
public class SmartFileProperties {

    /**
     * 默认的文件执行器
     */
    private ActualFileServiceEnum defaultHandler = ActualFileServiceEnum.DISK;

    /**
     * sftp配置
     */
    private SmartJschProperties sftp;

    private DiskProperties disk = new DiskProperties();

    private MinioProperties minio = new MinioProperties();

    @Getter
    @Setter
    public static class SmartJschProperties {

        /**
         * 用户名
         */
        private String username;

        /**
         * 密码
         */
        private String password;

        /**
         * 私钥
         */
        private String privateKey;

        /**
         * ip
         */
        private String host;

        private Integer port = 22;

        private String channel = "sftp";

        private String basePath;
    }

    /**
     * 本地磁盘存储配置
     */
    @Getter
    @Setter
    public static class DiskProperties {
        /**
         * 本地磁盘存储路径
         */
        private String basePath;
    }

    @Getter
    @Setter
    public static class MinioProperties {
        private String endpoint;

        private String accessKey;

        private String secretKey;

        private String bucketName;
    }
}
