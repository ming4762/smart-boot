package com.smart.framework.file.extensions.sftp.sshj.pool;

import com.smart.framework.file.core.properties.SmartFileStorageSftpSshjProperties;
import lombok.Getter;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

import java.io.Closeable;
import java.io.IOException;

/**
 * SFTP 连接
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-21 18:37
 * @since 5.0.0
 */
public class SmartSftpConnection implements Closeable {

    private final SSHClient  ssh;
    @Getter
    private final SFTPClient sftp;

    public SmartSftpConnection(SmartFileStorageSftpSshjProperties config) throws IOException {
        ssh = new SSHClient();
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        if (config.getConnectTimeout() != null) {
            ssh.setConnectTimeout(config.getConnectTimeout());
        }
        ssh.connect(config.getHost(), config.getPort());

        // 认证方式
        if (config.getPrivateKeyPath() != null) {
            ssh.authPublickey(config.getUsername(), config.getPrivateKeyPath());
        } else {
            ssh.authPassword(config.getUsername(), config.getPassword());
        }

        sftp = ssh.newSFTPClient();
    }

    /**
     * 检查连接是否仍然有效
     * @return 连接是否有效
     */
    public boolean isConnected() {
        return ssh.isConnected() && ssh.isAuthenticated();
    }

    @Override
    public void close() throws IOException {
        if (sftp != null) {
            sftp.close();
        }
        if (ssh  != null) {
            ssh.disconnect();
        }
    }
}
