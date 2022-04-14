package com.alibaba.chaosblade.box.common.experiment.task.flow.util;

import com.google.common.base.Strings;
import org.apache.commons.validator.routines.RegexValidator;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * @author haibin
 *
 *
 */
public class SystemUtils {

    public static String LOCAL_ADDRESS = localAddress();

    public static int PID = pid();

    private static final Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");


    private static final RegexValidator subMaskValidator = new RegexValidator(
            "^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})/(\\d{1,3})$");

    public static boolean isValidIpAddress(String ipAddress) {
        if (Strings.isNullOrEmpty(ipAddress)) {
            return false;
        }
        return isIpAddress(ipAddress);
    }

    public static boolean isIpAddress(String address) {
        return isIpv4Address(address) || isIpv6Address(address);
    }

    public static boolean isIpv4Address(String host) {
        return host != null && IPV4_PATTERN.matcher(host).matches();
    }

    public static boolean isIpv6Address(String host) {
        if (host != null && !host.isEmpty()) {
            if (host.charAt(0) != '[') {
                return false;
            } else {
                return host.length() > 2 && host.charAt(host.length() - 1) == ']';
            }
        } else {
            return false;
        }
    }

    /**
     * 粗略判断
     *
     * @param subMask
     * @return
     */
    public static boolean isValidIpSubMask(String subMask) {
        if (Strings.isNullOrEmpty(subMask)) {
            return false;
        }
        return subMaskValidator.isValid(subMask);
    }

    public static String getLocalAddress() {
        return LOCAL_ADDRESS;
    }

    public static int getCurrentPid() {
        return PID;
    }

    private static int pid() {
        try {
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            String name = runtime.getName();
            return Integer.parseInt(name.substring(0, name.indexOf('@')));
        } catch (Throwable t) {
            return 0;
        }
    }

    private static String localAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress address = null;
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                // 排除掉虚拟网卡的 IP，@since 1.3.5.2
                String displayName = ni.getDisplayName();
                if (displayName != null && displayName.startsWith("virbr")) {
                    continue;
                }
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address.getHostAddress().indexOf(":") == -1) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (Throwable t) {
        }
        return "127.0.0.1";
    }

    public static int getCpuProcessorCount() {
        return Runtime.getRuntime().availableProcessors();
    }
}
