package com.smart.file.extensions.ftp.pool;

import com.smart.file.core.properties.SmartFileStorageFtpProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * @author shizhongming
 * 2024/2/18 21:33
 * @since 3.0.0
 */
@Slf4j
public class FtpClientKeyedPooledObjectFactory implements KeyedPooledObjectFactory<SmartFileStorageFtpProperties, FTPClient> {

    private static final String DEFAULT_USER_NAME = "Anonymous";

    @Override
    public void activateObject(SmartFileStorageFtpProperties key, PooledObject<FTPClient> p) throws Exception {
        // do nothing
    }

    @Override
    public void destroyObject(SmartFileStorageFtpProperties key, PooledObject<FTPClient> p) throws Exception {
        FTPClient ftpClient = p.getObject();
        if (ftpClient != null && ftpClient.isConnected()) {
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }

    @Override
    public PooledObject<FTPClient> makeObject(SmartFileStorageFtpProperties key) throws Exception {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(key.getHost(), key.getPort());
        if (StringUtils.hasText(key.getUsername())) {
            ftpClient.login(key.getUsername(), key.getPassword());
        } else {
            ftpClient.login(DEFAULT_USER_NAME, "");
        }
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.setControlEncoding(Objects.requireNonNullElse(key.getEncoding(), "UTF-8"));
        ftpClient.enterLocalPassiveMode();
        return new DefaultPooledObject<>(ftpClient);
    }


    @Override
    public void passivateObject(SmartFileStorageFtpProperties key, PooledObject<FTPClient> p) throws Exception {
        // do nothing
    }

    @Override
    public boolean validateObject(SmartFileStorageFtpProperties key, PooledObject<FTPClient> p) {
        FTPClient ftpClient = p.getObject();
        if (ftpClient == null ||  !ftpClient.isConnected()) {
            return false;
        }
        try {
            boolean result = ftpClient.sendNoOp();
            if (result) {
                ftpClient.changeWorkingDirectory("/");
            }
            return result;
        } catch (IOException e) {
            log.error("验证ftp连接失败！", e);
            return false;
        }
    }
}
