package com.smart.commons.core.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
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
	 * 真实物理网卡名称列表
	 */
	private static final List<String> REAL_DISPLAY_NAME = List.of(
			"Intel",
			"Realtek",
			"Atheros",
			"Broadcom"
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
			if (org.apache.commons.lang3.StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (org.apache.commons.lang3.StringUtils.isEmpty(ip) || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (org.apache.commons.lang3.StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (org.apache.commons.lang3.StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (org.apache.commons.lang3.StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
				List<String> realLocalIpList = getRealLocalIpList();
				if (!realLocalIpList.isEmpty()) {
					ip = realLocalIpList.get(0);
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
		List<String> realIpList = new LinkedList<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaces.nextElement();
				// 去除回环接口，子接口，未运行接口
				if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
					continue;
				}
				// 判断网卡名字是否是真实网卡名字
				String displayName = networkInterface.getDisplayName();
				boolean match = REAL_DISPLAY_NAME.stream()
						.anyMatch(displayName::contains);
				if (!match) {
					continue;
				}
				Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
				while (inetAddresses.hasMoreElements()) {
					InetAddress inetAddress = inetAddresses.nextElement();
					if (!(inetAddress instanceof Inet4Address)) {
						continue;
					}
					realIpList.add(inetAddress.getHostAddress());
				}
			}
        } catch (SocketException e) {
			log.error(e.getMessage(), e);
			return Collections.emptyList();
        }
        return realIpList;
	}

	public static void main(String[] args) {
		List<String> localIpList = getRealLocalIpList();
		localIpList.forEach(System.out::println);
	}
}
