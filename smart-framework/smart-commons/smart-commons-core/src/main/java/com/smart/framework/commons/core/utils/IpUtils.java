package com.smart.framework.commons.core.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.*;
import java.util.*;


/**
 * IP地址
 * @author jackson
 */
@Slf4j
public class IpUtils {

    private static final String UNKNOWN = "unknown";

    private static final String LOCALHOST_IP = "127.0.0.1";
    /**
     * 客户端与服务器同为一台机器，获取的 ip 有时候是 ipv6 格式
     */
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
    private static final String SEPARATOR = ",";

    /**
     * 常见的物理/主用网卡名字前缀（作为偏好匹配，用于不同系统）
     * 注意：这只是偏好，不作为必须条件。
     */
    private static final List<String> PREFERRED_IF_PREFIX = List.of(
            "en",   // macOS / some Linux
            "eth",  // Linux
            "enp",  // new Linux naming
            "ens",  // another Linux
            "wlan", // Windows/Linux wifi
            "wl"    // some wifi names
    );

    private IpUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取IP地址
     * <p>
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String getIpAddress() {
        return Optional.ofNullable(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()))
                .map(ServletRequestAttributes::getRequest)
                .map(IpUtils::getIpAddr)
                .orElse("");
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            boolean ipIsNull = !StringUtils.hasText(ip);
            if (ipIsNull || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ipIsNull || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipIsNull || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ipIsNull || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ipIsNull || UNKNOWN.equalsIgnoreCase(ip)) {
                List<String> realLocalIpList = getRealLocalIpList();
                if (!realLocalIpList.isEmpty()) {
                    ip = realLocalIpList.getFirst();
                }
            }
        } catch (Exception e) {
            log.error("IPUtils ERROR ", e);
        }
        // 对于通过多个代理的情况，分割出第一个 IP
        if (ip != null && ip.length() > 15) {
            if (ip.contains(SEPARATOR)) {
                ip = ip.substring(0, ip.indexOf(SEPARATOR));
            }
        }
        return LOCALHOST_IPV6.equals(ip) ? LOCALHOST_IP : ip;
    }


    /**
     * 获取本机所有网卡IP地址 包括虚拟网卡
     * @return IP地址列表
     */
    public static List<String> getLocalIpList() {
        List<String> ipList = new ArrayList<>(16);
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (inetAddress instanceof Inet4Address) {
                        ipList.add(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            log.error(e.getMessage(), e);
        }
        return ipList;
    }

    /**
     * 获取真实IP地址（真实物理网卡）
     * @return IP地址列表
     */
    @NonNull
    public static List<String> getRealLocalIpList() {
        // 有优先匹配接口名字或 site-local 的地址
        List<String> preferred = new ArrayList<>();
        // 其它可用 IPv4 地址（非 loopback/link-local）
        List<String> fallback = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                // 去除回环接口，子接口，未运行接口
                if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                    continue;
                }

                String name = networkInterface.getName() != null ? networkInterface.getName() : "";
                String display = networkInterface.getDisplayName() != null ? networkInterface.getDisplayName() : "";

                boolean prefName = PREFERRED_IF_PREFIX.stream()
                        .anyMatch(prefix -> name.startsWith(prefix) || display.startsWith(prefix));

                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!(inetAddress instanceof Inet4Address)) {
                        continue;
                    }
                    if (inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress()) {
                        continue;
                    }
                    String ip = inetAddress.getHostAddress();
                    if (inetAddress.isSiteLocalAddress() || prefName) {
                        if (!preferred.contains(ip)) {
                            preferred.add(ip);
                        }
                    } else {
                        if (!fallback.contains(ip)) {
                            fallback.add(ip);
                        }
                    }
                }
            }
        } catch (SocketException e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
        if (!preferred.isEmpty()) {
            return preferred;
        }
        if (!fallback.isEmpty()) {
            return fallback;
        }
        // 最后没有结果，返回空列表
        return Collections.emptyList();
    }
}
