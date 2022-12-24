package com.smart.commons.server.info;

import lombok.SneakyThrows;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Optional;

/**
 * @author shizhongming
 * 2022/12/4 16:57
 */
public class ServerInfoProvider {

    private ServerInfoProvider() {
        throw new IllegalStateException("Utility class");
    }

    private static ServerInfo serverInfo = null;

    static {
        initServerInfo();
    }

    /**
     * 获取CPU序列号
     *
     * @return cpu序列号
     */
    public static String getCpuSerial() {
        return serverInfo.getCpuSerial();
    }

    /**
     * 获取主板序列号
     *
     * @return 主板序列号
     */
    public static String getMainBoardSerial() {
        return serverInfo.getMainBoardSerial();
    }

    public static List<String> getMacAddress() {
        return serverInfo.getMacAddress();
    }

    public static List<String> getIpAddress() {
        return serverInfo.getIpAddress();
    }

    @SneakyThrows
    private static void initServerInfo() {
        if (serverInfo == null) {
            if (isLinux()) {
                serverInfo = new LinuxServerInfo();
            } else if (isWindows()) {
                serverInfo = new WindowsServerInfo();
            } else {
                throw new OperationNotSupportedException("不支持的操作系统");
            }
        }
    }

    /**
     * 判断服务器是否是windows
     * @return 是否是windows
     */
    public static boolean isWindows() {
        return Optional.ofNullable(getOs())
                .map(item -> item.toLowerCase().startsWith("windows"))
                .orElse(false);
    }

    /**
     * 判断服务器是否是linux
     * @return 是否是linux
     */
    public static boolean isLinux() {
        return Optional.ofNullable(getOs())
                .map(item -> item.toLowerCase().startsWith("linux"))
                .orElse(false);
    }


    private static String getOs() {
        return System.getProperty("os.name");
    }
}
