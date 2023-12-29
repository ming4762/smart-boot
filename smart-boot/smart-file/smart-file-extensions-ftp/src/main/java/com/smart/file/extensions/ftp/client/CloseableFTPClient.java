package com.smart.file.extensions.ftp.client;

import com.smart.file.core.properties.SmartFileStorageFtpProperties;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.util.StringUtils;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketException;

/**
 * @author shizhongming
 * 2023/3/21 18:44
 * @since 3.0.0
 */
@Getter
public class CloseableFTPClient implements Closeable {

    private static final String DEFAULT_USER_NAME = "Anonymous";

    private final FTPClient ftpClient;

    @SneakyThrows({SocketException.class, IOException.class})
    public CloseableFTPClient(SmartFileStorageFtpProperties properties) {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(properties.getHost(), properties.getPort());
        if (StringUtils.hasText(properties.getUsername())) {
            ftpClient.login(properties.getUsername(), properties.getPassword());
        } else {
            ftpClient.login(DEFAULT_USER_NAME, "");
        }
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        this.ftpClient = ftpClient;
    }

    @Override
    public void close() throws IOException {
        if (this.ftpClient != null) {
            this.ftpClient.abort();
            this.ftpClient.logout();
            this.ftpClient.disconnect();
        }
    }
}
